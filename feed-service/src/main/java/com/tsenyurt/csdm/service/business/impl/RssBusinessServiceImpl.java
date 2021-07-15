package com.tsenyurt.csdm.service.business.impl;

import com.tsenyurt.csdm.domain.RSSItem;
import com.tsenyurt.csdm.repository.RssItemRepository;
import com.tsenyurt.csdm.service.business.RssBusinessService;
import com.tsenyurt.csdm.service.data.RssDataService;
import com.tsenyurt.csdm.view.RssItemView;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RssBusinessServiceImpl implements RssBusinessService {

  @Value("${rss.max.stored.record.count.in.db:10}")
  public Integer maxDbRecordAllowed;

  private RssDataService rssDataService;

  private RssItemRepository rssItemRepository;

  @Autowired
  public RssBusinessServiceImpl(RssDataService rssDataService,RssItemRepository rssItemRepository) {
    this.rssDataService = rssDataService;
    this.rssItemRepository = rssItemRepository;
  }

  public static Function<RssItemView, String> rssItemToUrl = item -> item.getUrl();

  @Override
  public void updateRssFeedsInDb() {
    try {
      List<RssItemView> latestRssFeedsFromExternalSource = rssDataService.getLatestRssFeedsFromExternalSource();
      List<String> urlLists = latestRssFeedsFromExternalSource.stream().map(rssItemToUrl).collect(Collectors.toList());
      List<RSSItem> dbRecordsOfUrls = rssItemRepository.findByUrlList(urlLists); // if returns from db it means already in db
      //findRecordsNotSavedYet
      //if already in DB check updateTime for update


      Long dbRowCount = rssItemRepository.countAllRecords();
      if (Long.compare(dbRowCount, ZERO_INT) == ZERO_INT) { // If there is none record save all according to
        // rss.max.stored.record.count.in.db param in app.prop
       /* rssItemRepository.saveAll(rssItemList);*/
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
