package com.tsenyurt.csdm.service.business.impl;

import java.util.*;
import java.util.concurrent.*;

import com.google.common.util.concurrent.*;
import com.tsenyurt.csdm.repository.*;
import com.tsenyurt.csdm.service.util.*;
import com.tsenyurt.csdm.view.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
        }catch (InterruptedException e) {
          log.error( "Interrupted!", e);
          Thread.currentThread().interrupt();
        }catch (Exception e) {
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
