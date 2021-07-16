package com.tsenyurt.csdm.listener;

import com.tsenyurt.csdm.service.business.RssBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContextInitializedListener implements ApplicationListener<ContextRefreshedEvent> {
  @Autowired private RssBusinessService businessService;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    businessService.startAsyncQueue();
  }
}
