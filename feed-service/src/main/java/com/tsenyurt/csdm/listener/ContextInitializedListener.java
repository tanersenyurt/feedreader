package com.tsenyurt.csdm.listener;

import static com.tsenyurt.csdm.service.ProcessType.ASYNC;

import com.tsenyurt.csdm.service.ProcessType;
import com.tsenyurt.csdm.service.business.impl.AsyncRssProcessServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnExpression("'${feed.process.type}'.equals('ASYNC')")
public class ContextInitializedListener implements ApplicationListener<ContextRefreshedEvent> {
  @Value("${feed.process.type:ASYNC}")
  public String processType;

  @Autowired private AsyncRssProcessServiceImpl asyncRssProcessService;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("#####=====> SPRING APPLICATION CONTEXT INITIALIZED  <=====#####");
      asyncRssProcessService.startAsyncQueue();
  }
}
