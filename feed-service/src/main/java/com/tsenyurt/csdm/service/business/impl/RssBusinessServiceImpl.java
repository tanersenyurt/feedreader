package com.tsenyurt.csdm.service.business.impl;

import static com.tsenyurt.csdm.service.ProcessType.ASYNC;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tsenyurt.csdm.domain.RSSItem;
import com.tsenyurt.csdm.repository.RssItemRepository;
import com.tsenyurt.csdm.service.business.RssBusinessService;
import com.tsenyurt.csdm.service.data.RssDataService;
import com.tsenyurt.csdm.service.util.ExceptionUtil;
import com.tsenyurt.csdm.view.RssItemView;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class RssBusinessServiceImpl implements RssBusinessService {

  @Value("${async.process.queue.size:100}")
  public Integer queueSize;

  @Value("${async.process.thread.size:3}")
  public Integer threadSize;

  @Value("${feed.process.type:ASYNC}")
  public String processType;

  private RssDataService rssDataService;

  private RssItemRepository rssItemRepository;

  @Getter private BlockingQueue<RssItemView> blockingQueue = new LinkedBlockingDeque<>(100);

  //Might be two business implementatiton like syncImpl and asyncImpl
  Runnable consumerTask =
      () -> {
        try {
          while (true) {
            RssItemView rssItemView = getBlockingQueue().take();
            log.info(
                String.format(
                    "ConsumerTask => for url: %s starting to process", rssItemView.getUrl()));
            try {
              RSSItem item = rssItemRepository.findByUrl(rssItemView.getUrl());
              if (item == null) // item must save
              {
                rssItemRepository.save(RssItemView.createEntity(rssItemView));

              } else if (rssItemView.getUpdateTime() != null
                  && item.getPublication()
                      .before(
                          rssItemView
                              .getUpdateTime())) // item updated in feed so must be updated in DB
              {
                updateRssItemInfos(rssItemView, item);
                rssItemRepository.save(item);
              }

            } catch (Exception e) {
              log.error(
                  String.format(
                      "ConsumerTask => error occur while saving/updating data: %s",
                      ExceptionUtil.convertStackTraceToString(e, 1000)));
            }
            Thread.sleep(new Random().nextInt(1500));
          }
        } catch (Exception e) {
          log.error(
              String.format(
                  "ConsumerTask => error occur while: %s",
                  ExceptionUtil.convertStackTraceToString(e, 1000)));
        }
      };

  private void updateRssItemInfos(RssItemView rssItemView, RSSItem item) {
    item.setDescription(rssItemView.getDescription());
    item.setTitle(rssItemView.getTitle());
    item.setUpdateTime(rssItemView.getUpdateTime());
  }

  private ThreadFactory namedThreadFactory =
      new ThreadFactoryBuilder().setNameFormat("AsyncRssProcessor").build();
  @Getter private ExecutorService executor = Executors.newFixedThreadPool(10, namedThreadFactory);

  @Autowired
  public RssBusinessServiceImpl(
      RssDataService rssDataService, RssItemRepository rssItemRepository) {
    this.rssDataService = rssDataService;
    this.rssItemRepository = rssItemRepository;
  }

  @Override
  @Scheduled(fixedRateString = "${rss.fetch.in.milliseconds:300000}") // 5*60*1000 => 5 minutes
  public void updateRssFeedsInDb() {
    log.info(String.format("updateRssFeedsInDb -----> STARTED ::: [ %s ]", processType));
    if (ASYNC.name().equalsIgnoreCase(processType)) {
      List<RssItemView> extFeeds = rssDataService.getLatestRssFeedsFromExternalSource();
      for (RssItemView extFeed : extFeeds) {
        getBlockingQueue().add(extFeed);
      }
    } else {
      syncProcess();
    }
  }

  private void syncProcess() {
    try {
      List<RssItemView> latestRssFeedsFromExternalSource =
          rssDataService.getLatestRssFeedsFromExternalSource();
      List<String> urlLists =
          latestRssFeedsFromExternalSource.stream().map(rssItemToUrl).collect(Collectors.toList());
      List<RSSItem> dbRecordsOfUrls =
          rssItemRepository.findByUrlList(urlLists); // if returns from db it means already in db

      if (dbRecordsOfUrls.size()
          == ZERO_INT) // there is no record with according url in db so we can save all
      {
        List<RSSItem> feedsToSave =
            latestRssFeedsFromExternalSource.stream()
                .map(RssItemView::createEntity)
                .collect(Collectors.toList());
        rssItemRepository.saveAll(feedsToSave);

      } else {
        // Check stored feed updated ?
        for (RSSItem dbFeed : dbRecordsOfUrls) {
          for (RssItemView extFeed : latestRssFeedsFromExternalSource) {
            boolean isFeedUpdated =
                extFeed.getUpdateTime() != null
                    && extFeed.getUpdateTime().after(dbFeed.getPublication());
            if (dbFeed.getUrl().equals(extFeed.getUrl())
                && isFeedUpdated) { // then is must bu updated in db too
              updateRssItemInfos(extFeed, dbFeed);
              try {
                rssItemRepository.save(dbFeed);
              } catch (Exception e) {
                log.error(
                    String.format(
                        "while updating url[%s] and id[%d] entity failed! ",
                        dbFeed.getUrl(), dbFeed.getId()));
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.error("while updateRssFeedsInDb method processing exception happened " + e.getMessage());
    }
  }

  @Override
  public void startAsyncQueue() {
    for (int i = 0; i < threadSize; i++) {
      executor.execute(consumerTask);
    }
    log.info("startAsyncQueue -----> STARTED");
  }
}
