package com.tsenyurt.csdm.service.business.impl;

import com.tsenyurt.csdm.domain.RSSItem;
import com.tsenyurt.csdm.view.RssItemView;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("syncRssProcessService")
@RequiredArgsConstructor
@Slf4j
public class SyncRssProcessServiceImpl extends BaseRssProcessImpl {

  @Override
  public void process(List<RssItemView> rssItemViewList) {
    try {
      List<String> urlLists =
          rssItemViewList.stream().map(rssItemToUrl).collect(Collectors.toList());
      List<RSSItem> dbRecordsOfUrls =
          rssItemRepository.findByUrlList(urlLists); // if returns from db it means already in db

      if (dbRecordsOfUrls.size()
          == ZERO_INT) // there is no record with according url in db so we can save all
      {
        List<RSSItem> feedsToSave =
            rssItemViewList.stream().map(RssItemView::createEntity).collect(Collectors.toList());
        rssItemRepository.saveAll(feedsToSave);

      } else {
        for (RssItemView item : rssItemViewList) {
          processRssItem(item);
        }
      }
    } catch (Exception e) {
      log.error("while updateRssFeedsInDb method processing exception happened " + e.getMessage());
    }
  }
}
