package com.tsenyurt.csdm.service.business.impl;

import java.util.function.*;

import com.tsenyurt.csdm.domain.*;
import com.tsenyurt.csdm.repository.*;
import com.tsenyurt.csdm.service.business.*;
import com.tsenyurt.csdm.service.util.*;
import com.tsenyurt.csdm.view.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseRssProcessImpl implements BaseRssProcess{

  private final RssItemRepository rssItemRepository;

  public static final Function<RssItemView, String> RSS_ITEM_TO_URL = RssItemView::getUrl;

  public void updateRssItemInfos(RssItemView rssItemView, RSSItem item) {
    item.setDescription(rssItemView.getDescription());
    item.setTitle(rssItemView.getTitle());
    item.setUpdateTime(rssItemView.getUpdateTime());
  }

  public void processRssItem(RssItemView rssItemView) {
    try {
      RSSItem item = rssItemRepository.findByUrl(rssItemView.getUrl());
      if (item == null) // item must save
      {
        rssItemRepository.save(RssItemView.createEntity(rssItemView));

      } else // @Because we use @DynamicUpdate if there is change then it will update
      {
        updateRssItemInfos(rssItemView, item);
        rssItemRepository.save(item);
      }

    } catch (Exception e) {
      log.error(
          String.format(
              "BaseRssProcessImpl.processRssItem => error occur while saving/updating data: %s",
              ExceptionUtil.convertStackTraceToString(e, 1000)));
    }
  }
}
