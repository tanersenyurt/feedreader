package com.tsenyurt.csdm.service.business.impl;

import java.util.*;

import com.tsenyurt.csdm.service.*;
import com.tsenyurt.csdm.service.business.*;
import com.tsenyurt.csdm.service.data.*;
import com.tsenyurt.csdm.service.util.*;
import com.tsenyurt.csdm.view.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class RssBusinessServiceImpl implements RssBusinessService {
  protected final RssDataService rssDataService;

  @Value("${feed.process.type:ASYNC}")
  public String processType;

  private final ISpringApplicationContextUtil springContext;

  @Override
  @Scheduled(fixedRateString = "${rss.fetch.in.milliseconds:300000}") // 5*60*1000 => 5 minutes
  public void scheduledMethodToCall() {
    log.info(String.format("scheduledMethodToCall -----> TRIGGERED ::: [ %s ]", processType));
    process();
  }

  @Override
  public void process() {
    List<RssItemView> extFeeds = rssDataService.getLatestRssFeedsFromExternalSource();
    ProcessType typeOfProcess = ProcessType.findFromName(processType);
    BaseRssProcessService bean =
        (BaseRssProcessService) springContext.getBean(typeOfProcess.getBean());
    bean.process(extFeeds);
  }
}
