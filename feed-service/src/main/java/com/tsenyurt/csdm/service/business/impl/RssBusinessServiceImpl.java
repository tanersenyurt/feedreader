package com.tsenyurt.csdm.service.business.impl;

import com.google.common.collect.Lists;
import com.tsenyurt.csdm.domain.RSSItem;
import com.tsenyurt.csdm.repository.RssItemRepository;
import com.tsenyurt.csdm.service.business.RssBusinessService;
import com.tsenyurt.csdm.service.data.RssDataService;
import com.tsenyurt.csdm.view.RssItemView;
import java.util.List;
import java.util.stream.Collectors;
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

  @Value("${rss.max.stored.record.count.in.db:10}")
  public Integer maxDbRecordAllowed;

  private RssDataService rssDataService;

  private RssItemRepository rssItemRepository;

  @Autowired
  public RssBusinessServiceImpl(
      RssDataService rssDataService, RssItemRepository rssItemRepository) {
    this.rssDataService = rssDataService;
    this.rssItemRepository = rssItemRepository;
  }

  @Override
  @Scheduled(fixedRateString = "${rss.fetch.in.milliseconds:300000}") // 5*60*1000 => 5 minutes
  public void updateRssFeedsInDb() {
    try {
      List<RssItemView> latestRssFeedsFromExternalSource =  rssDataService.getLatestRssFeedsFromExternalSource();
      Long dbcount = rssItemRepository.countAllRecords();
      List<String> urlLists = latestRssFeedsFromExternalSource.stream().map(rssItemToUrl).collect(Collectors.toList());
      List<RSSItem> dbRecordsOfUrls = rssItemRepository.findByUrlList(urlLists); // if returns from db it means already in db

      if (dbRecordsOfUrls.size() == 0 && Long.compare(dbcount, 0l) == 0) // there is no record with according url in db so we can save all
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
            boolean isFeedUpdated =  extFeed.getUpdateTime() != null  && extFeed.getUpdateTime().after(dbFeed.getPublication());
            if (dbFeed.getUrl().equals(extFeed.getUrl())  && isFeedUpdated) { // then is must bu updated in db too
              dbFeed.setDescription(extFeed.getDescription());
              dbFeed.setTitle(extFeed.getTitle());
              dbFeed.setUpdateTime(extFeed.getUpdateTime());
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

        // Not in db so will be added but db records count must be limited according to
        // rss.max.stored.record.count.in.db param
        List<RssItemView> fromSourceList =
            Lists.newArrayList(
                latestRssFeedsFromExternalSource); // In the end , there will be only the records
                                                   // not stored yet
        for (RSSItem dbfeed : dbRecordsOfUrls) {
          for (RssItemView extFeed : latestRssFeedsFromExternalSource) {
            if (dbfeed.getUrl().equals(extFeed.getUrl())) {
              fromSourceList.remove(extFeed);
            }
          }
        }

        for (RssItemView feedsToSave : fromSourceList) {
          try {
            saveNewRssFeed(feedsToSave);
          } catch (Exception e) {
            log.error(
                String.format(
                    "while saving url[%s] entity error occurd!  [%s]",
                    feedsToSave.getUrl(), e.getMessage()));
          }
        }
      }

    } catch (Exception e) {
      log.error("while updateRssFeedsInDb method processing exception happened " + e.getMessage());
    }
  }

  /**
   * //TODO: Should be in different service for @Transactional purpose
   *
   * @param feedsToSave
   */
  private void saveNewRssFeed(RssItemView feedsToSave) {
    Long count = rssItemRepository.countAllRecords();
    if (Long.compare(count, 0l) == 1) {
      rssItemRepository.removeOldestRecord();
    }
    rssItemRepository.save(feedsToSave.createEntity(feedsToSave));
  }
}
