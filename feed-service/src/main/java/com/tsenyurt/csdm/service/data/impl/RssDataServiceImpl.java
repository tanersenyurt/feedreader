package com.tsenyurt.csdm.service.data.impl;

import java.net.*;
import java.util.*;
import java.util.stream.*;

import com.google.common.collect.*;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.*;
import com.tsenyurt.csdm.service.data.*;
import com.tsenyurt.csdm.service.data.impl.exception.*;
import com.tsenyurt.csdm.view.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RssDataServiceImpl implements RssDataService {

  @Value("${external.feed.url}")
  public String rssFeedUrl;

  @Override
  public List<RssItemView> readFromExternalFeed() throws FeedReadException {
    List<RssItemView> rssItems = Lists.newArrayList();
    try {
      URL feedSource = new URL(rssFeedUrl);
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feed = input.build(new XmlReader(feedSource));
      rssItems = feed.getEntries().stream().map(RssItemView::createInstance).collect(Collectors.toList());
    } catch (Exception e) {
      log.error(
          String.format(
              "RssDataServiceImpl.readFromExternalFeed() during http call exception happened! Reason:[%s]",
              e.getMessage()));
      throw new FeedReadException(e.getMessage());
    }
    return rssItems;
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
