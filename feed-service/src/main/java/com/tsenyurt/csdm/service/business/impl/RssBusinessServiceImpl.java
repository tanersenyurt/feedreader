package com.tsenyurt.csdm.service.business.impl;

import com.tsenyurt.csdm.service.ProcessType;
import com.tsenyurt.csdm.service.business.BaseRssProcessService;
import com.tsenyurt.csdm.service.business.RssBusinessService;
import com.tsenyurt.csdm.service.data.RssDataService;
import com.tsenyurt.csdm.service.util.ISpringApplicationContextUtil;
import com.tsenyurt.csdm.view.RssItemView;
import java.util.List;
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
  protected RssDataService rssDataService;

  @Value("${feed.process.type:ASYNC}")
  public String processType;

  private ISpringApplicationContextUtil springContext;

  @Autowired
  public RssBusinessServiceImpl(
      final RssDataService rssDataService, final ISpringApplicationContextUtil springContext) {
    this.rssDataService = rssDataService;
    this.springContext = springContext;
  }

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
