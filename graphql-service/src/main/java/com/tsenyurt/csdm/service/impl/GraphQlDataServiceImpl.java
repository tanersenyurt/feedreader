package com.tsenyurt.csdm.service.impl;

import com.tsenyurt.csdm.domain.RSSItem;
import com.tsenyurt.csdm.repository.RssItemRepository;
import com.tsenyurt.csdm.service.GraphQlDataService;
import com.tsenyurt.csdm.view.RssItemView;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    List<RssItemView> rssItemViewList =
        rssItemList.stream().map(RSSItem::convertToView).collect(Collectors.toList()).stream()
            .sorted(Comparator.comparing(RssItemView::getPublication).reversed())
            .collect(Collectors.toList());

    return rssItemViewList;
  }
}
