package com.tsenyurt.csdm.service.business.impl;

import com.tsenyurt.csdm.domain.RSSItem;
import com.tsenyurt.csdm.repository.RssItemRepository;
import com.tsenyurt.csdm.service.business.RssBusinessService;
import com.tsenyurt.csdm.service.data.RssDataService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RssBusinessServiceImpl implements RssBusinessService {

  private RssDataService rssDataService;

  private RssItemRepository rssItemRepository;

  @Autowired
  public RssBusinessServiceImpl(RssDataService rssDataService,RssItemRepository rssItemRepository) {
    this.rssDataService = rssDataService;
    this.rssItemRepository = rssItemRepository;
  }


  @Override
  public void updateRssFeedsInDb() {
    try {
      List<RSSItem> rssItemList = rssDataService.readFromExternalFeed();
      Long dbRowCount = rssItemRepository.countAllRecords();
      if (Long.compare(dbRowCount, ZERO_INT) == ZERO_INT) { // If there is none record save all according to
        // rss.max.stored.record.count.in.db param in app.prop
        rssItemRepository.saveAll(rssItemList);
      } else {
        List<RSSItem> allRecordsInDb = rssItemRepository.findAll();
        List<RSSItem> collect =
            allRecordsInDb.stream()
                .sorted(Comparator.comparing(RSSItem::getPublication).reversed())
                .collect(Collectors.toList());

        /*collect.forEach(x -> System.err.println(String.format(" %s %s", x.getTitle(), x.getPublicationAsString())));*/
      }
    } catch (Exception e) {

    }
  }
}
