package com.tsenyurt.csdm.service.business.impl;

import com.tsenyurt.csdm.domain.RSSItem;
import com.tsenyurt.csdm.repository.RssItemRepository;
import com.tsenyurt.csdm.service.business.BaseRssProcessService;
import com.tsenyurt.csdm.service.util.ExceptionUtil;
import com.tsenyurt.csdm.view.RssItemView;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public abstract class BaseRssProcessImpl implements BaseRssProcessService {

  protected RssItemRepository rssItemRepository;

  public static Function<RssItemView, String> rssItemToUrl = item -> item.getUrl();

  @Autowired
  public BaseRssProcessImpl(final RssItemRepository rssItemRepository) {
    this.rssItemRepository = rssItemRepository;
  }

  protected void updateRssItemInfos(RssItemView rssItemView, RSSItem item) {
    item.setDescription(rssItemView.getDescription());
    item.setTitle(rssItemView.getTitle());
    item.setUpdateTime(rssItemView.getUpdateTime());
  }

  protected void processRssItem(RssItemView rssItemView) {
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
