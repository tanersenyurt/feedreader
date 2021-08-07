package com.tsenyurt.csdm.service.business.impl;

import static com.tsenyurt.csdm.service.business.impl.BaseRssProcessImpl.*;

import java.util.*;
import java.util.stream.*;

import com.tsenyurt.csdm.domain.*;
import com.tsenyurt.csdm.repository.*;
import com.tsenyurt.csdm.service.business.*;
import com.tsenyurt.csdm.view.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("syncRssProcessService")
@RequiredArgsConstructor
@Slf4j
@ConditionalOnExpression("'${feed.process.type}'.equals('SYNC')")
public class SyncRssProcessServiceImpl implements RssProcessService {

  private final RssItemRepository rssItemRepository;
  private final BaseRssProcess baseRssProcess;

  @Override
  @Transactional
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
          baseRssProcess.processRssItem(item);
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
