package com.tsenyurt.csdm.service.impl;

import java.util.*;
import java.util.stream.*;

import com.tsenyurt.csdm.domain.*;
import com.tsenyurt.csdm.repository.*;
import com.tsenyurt.csdm.service.*;
import com.tsenyurt.csdm.view.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GraphQlDataServiceImpl implements GraphQlDataService, GraphQLQueryResolver {
  // https://www.pluralsight.com/guides/building-a-graphql-server-with-spring-boot
  private RssItemRepository repository;

  @Autowired
  public GraphQlDataServiceImpl(RssItemRepository repository) {
    this.repository = repository;
  }

  public RssItemView rssItem(String url) {
    RSSItem rssItem = repository.findByUrl(url);
    return RSSItem.convertToView(rssItem);
  }

  public List<RssItemView> allRSSItems() {
    List<RSSItem> rssItemList = repository.findAll();
    return rssItemList.stream().map(RSSItem::convertToView).collect(Collectors.toList()).stream()
        .sorted(Comparator.comparing(RssItemView::getPublication).reversed())
        .collect(Collectors.toList());
  }
}
