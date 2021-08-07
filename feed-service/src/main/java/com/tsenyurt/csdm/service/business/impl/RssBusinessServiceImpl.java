package com.tsenyurt.csdm.service.business.impl;

import java.util.*;

import com.tsenyurt.csdm.service.business.*;
import com.tsenyurt.csdm.service.data.*;
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
  private final RssDataService rssDataService;
  private final RssProcessService relatedRssService;

  @Value("${feed.process.type:ASYNC}")
  public String processType;

  @Override
  @Scheduled(fixedRateString = "${rss.fetch.in.milliseconds:300000}") // 5*60*1000 => 5 minutes
  public void scheduledMethodToCall() {
    log.info(String.format("scheduledMethodToCall -----> TRIGGERED ::: [ %s ]", processType));
    process();
  }

  @Override
  public void process() {
    List<RssItemView> extFeeds = rssDataService.getLatestRssFeedsFromExternalSource();
    relatedRssService.process(extFeeds);
  }
}
