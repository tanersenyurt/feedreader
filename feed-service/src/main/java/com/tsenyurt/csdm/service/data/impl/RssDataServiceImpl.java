package com.tsenyurt.csdm.service.data.impl;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.tsenyurt.csdm.domain.RSSItem;
import com.tsenyurt.csdm.repository.RssItemRepository;
import com.tsenyurt.csdm.service.data.RssDataService;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RssDataServiceImpl implements RssDataService {

  @Value("${external.feed.url}")
  public String rssFeedUrl;

  @Value("${rss.max.stored.record.count.in.db:10}")
  public Integer maxDbRecordAllowed;

  private RssItemRepository rssItemRepository;

  @Autowired
  public RssDataServiceImpl(RssItemRepository rssItemRepository) {
    this.rssItemRepository = rssItemRepository;
  }


  @Override
  public List<RSSItem> readFromExternalFeed() throws Exception {
    URL feedSource = new URL(rssFeedUrl);
    SyndFeedInput input = new SyndFeedInput();
    try {
      SyndFeed feed = input.build(new XmlReader(feedSource));
      List<RSSItem> rssItems =
          feed.getEntries().stream().map(RSSItem::createInstance).collect(Collectors.toList());

      rssItemRepository.saveAll(rssItems);
      return rssItems;
    } catch (FeedException e) {
      e.printStackTrace();
    }
    return null;
  }
}
