package com.tsenyurt.csdm.service.business.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tsenyurt.csdm.repository.RssItemRepository;
import com.tsenyurt.csdm.service.util.ExceptionUtil;
import com.tsenyurt.csdm.view.RssItemView;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("asyncRssProcessService")
@Slf4j
public class AsyncRssProcessServiceImpl extends BaseRssProcessImpl {

  @Value("${async.process.queue.size:100}")
  public Integer queueSize;

  @Value("${async.process.thread.size:10}")
  public Integer threadSize;

  private ThreadFactory namedThreadFactory =
      new ThreadFactoryBuilder().setNameFormat("AsyncRssProcessor").build();
  @Getter private ExecutorService executor = Executors.newFixedThreadPool(10, namedThreadFactory);

  @Getter private BlockingQueue<RssItemView> blockingQueue = new LinkedBlockingDeque<>(100);

  @Autowired
  public AsyncRssProcessServiceImpl(RssItemRepository rssItemRepository) {
    super(rssItemRepository);
  }

  Runnable consumerTask =
      () -> {
        try {
          while (true) {
            RssItemView rssItemView = getBlockingQueue().take();
            log.info(
                String.format(
                    "ConsumerTask => for url: %s starting to process", rssItemView.getUrl()));
            processRssItem(rssItemView);
            log.info(
                String.format(
                    "ConsumerTask => for url: %s ending to process", rssItemView.getUrl()));
            Thread.sleep(new Random().nextInt(1500));
          }
        } catch (Exception e) {
          log.error(
              String.format(
                  "ConsumerTask => error occur while: %s",
                  ExceptionUtil.convertStackTraceToString(e, 1000)));
        }
      };

  public void startAsyncQueue() {
    log.info("Async processing method is selected so preparing executor threads");
    for (int i = 0; i < threadSize; i++) {
      executor.execute(consumerTask);
    }
  }

  @Override
  public void process(List<RssItemView> rssItemViewList) {
    rssItemViewList.forEach(item -> getBlockingQueue().add(item));
  }
}
