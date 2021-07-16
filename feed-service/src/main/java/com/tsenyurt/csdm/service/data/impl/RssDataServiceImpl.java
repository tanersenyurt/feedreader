package com.tsenyurt.csdm.service.data.impl;

import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.tsenyurt.csdm.service.data.RssDataService;
import com.tsenyurt.csdm.view.RssItemView;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RssDataServiceImpl implements RssDataService {

  @Value("${external.feed.url}")
  public String rssFeedUrl;

  @Autowired
  public RssDataServiceImpl() {}

  @Override
  public List<RssItemView> readFromExternalFeed() throws Exception {
    URL feedSource = new URL(rssFeedUrl);
    SyndFeedInput input = new SyndFeedInput();
    try {
      SyndFeed feed = input.build(new XmlReader(feedSource));
      List<RssItemView> rssItems =
          feed.getEntries().stream().map(RssItemView::createInstance).collect(Collectors.toList());

      return rssItems;
    } catch (FeedException e) {
      log.error(
          String.format(
              "RssDataServiceImpl.readFromExternalFeed() during http call exception happened! Reason:[%s]",
              e.getMessage()));
    }
    return null;
  }

  @Override
  public List<RssItemView> getLatestRssFeedsFromExternalSource() {
    List<RssItemView> resultList = Lists.newArrayList();
    try {
      List<RssItemView> rssItemViews = readFromExternalFeed();
      resultList =
          rssItemViews.stream()
              .sorted(Comparator.comparing(RssItemView::getPublication).reversed())
              .collect(Collectors.toList());

    } catch (Exception e) {
      log.error(
          String.format(
              "RssDataServiceImpl.getLatestRssFeedsFromExternalSource() during http call exception happened! Reason:[%s]",
              e.getMessage()));
    }
    return resultList;
  }
}
