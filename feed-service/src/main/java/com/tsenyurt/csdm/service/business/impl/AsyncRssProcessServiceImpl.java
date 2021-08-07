package com.tsenyurt.csdm.service.business.impl;

import static com.tsenyurt.csdm.service.ProcessType.*;

import java.security.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.common.util.concurrent.*;
import com.tsenyurt.csdm.service.*;
import com.tsenyurt.csdm.service.business.*;
import com.tsenyurt.csdm.service.util.*;
import com.tsenyurt.csdm.view.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("asyncRssProcessService")
@RequiredArgsConstructor
@Slf4j
@ConditionalOnExpression("'${feed.process.type}'.equals('ASYNC')")
public class AsyncRssProcessServiceImpl implements RssProcessService {

  public final BaseRssProcess baseRssProcess;

  @Value("${feed.process.type:ASYNC}")
  public String processType;

  @Value("${async.process.queue.size:100}")
  public Integer queueSize;

  @Value("${async.process.thread.size:10}")
  public Integer threadSize;

  private ThreadFactory namedThreadFactory =
      new ThreadFactoryBuilder().setNameFormat("AsyncRssProcessor").build();
  @Getter private ExecutorService executor = Executors.newFixedThreadPool(10, namedThreadFactory);

  @Getter private BlockingQueue<RssItemView> blockingQueue = new LinkedBlockingDeque<>(100);

  Runnable consumerTask =
      new Runnable() {
        @Override
        public void run() {
          try {
            while (true) {
              RssItemView rssItemView = AsyncRssProcessServiceImpl.this.getBlockingQueue().take();
              log.info(
                  String.format(
                      "ConsumerTask => for url: %s starting to process", rssItemView.getUrl()));
              baseRssProcess.processRssItem(rssItemView);
              log.info(
                  String.format(
                      "ConsumerTask => for url: %s ending to process", rssItemView.getUrl()));
              Thread.sleep(new SecureRandom().nextInt(1500));
              if(SYNC.name().equals(processType))
              {
                break;
              }
            }
          } catch (InterruptedException e) {
            log.error("Interrupted!", e);
            Thread.currentThread().interrupt();
          } catch (Exception e) {
            log.error(
                String.format(
                    "ConsumerTask => error occur while: %s",
                    ExceptionUtil.convertStackTraceToString(e, 1000)));
          }
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
