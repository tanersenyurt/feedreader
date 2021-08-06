package com.tsenyurt.csdm.service.business.impl;

import java.util.*;
import java.util.stream.*;

import com.tsenyurt.csdm.domain.*;
import com.tsenyurt.csdm.repository.*;
import com.tsenyurt.csdm.view.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("syncRssProcessService")
@RequiredArgsConstructor
@Slf4j
public class SyncRssProcessServiceImpl extends BaseRssProcessImpl {

  @Autowired
  public SyncRssProcessServiceImpl(RssItemRepository rssItemRepository) {
    super(rssItemRepository);
  }

  @Override
  public void process(List<RssItemView> rssItemViewList) {
    try {
      List<String> urlLists =
          rssItemViewList.stream().map(RSS_ITEM_TO_URL).collect(Collectors.toList());
      List<RSSItem> dbRecordsOfUrls =
          rssItemRepository.findByUrlList(urlLists); // if returns from db it means already in db

      if (dbRecordsOfUrls.size()
          == ZERO_INT) // there is no record with according url in db so we can save all
      {
        log.info("SyncRssProcessServiceImpl.process => for all rssitems to process");
        List<RSSItem> feedsToSave =
            rssItemViewList.stream().map(RssItemView::createEntity).collect(Collectors.toList());
        rssItemRepository.saveAll(feedsToSave);
        log.info("SyncRssProcessServiceImpl.process => for all rssitems to saved");

      } else {
        for (RssItemView item : rssItemViewList) {
          log.info(
              String.format(
                  "SyncRssProcessServiceImpl.process => for url: %s starting to process",
                  item.getUrl()));
          processRssItem(item);
          log.info(
              String.format(
                  "SyncRssProcessServiceImpl.process => for url: %s ending to process",
                  item.getUrl()));
        }
      }
    } catch (Exception e) {
      log.error("while updateRssFeedsInDb method processing exception happened " + e.getMessage());
    }
  }
}
