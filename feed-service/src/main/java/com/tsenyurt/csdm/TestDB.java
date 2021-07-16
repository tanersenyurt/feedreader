package com.tsenyurt.csdm;

import com.tsenyurt.csdm.domain.RSSItem;
import com.tsenyurt.csdm.repository.RssItemRepository;
import com.tsenyurt.csdm.service.business.RssBusinessService;
import com.tsenyurt.csdm.service.data.RssDataService;
import java.time.Instant;
import java.util.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TestDB {
  @Bean
  CommandLineRunner initDatabase(RssItemRepository repository, RssDataService rssDataService, RssBusinessService businessService) {
    return args -> {
      /*RSSItem item = RSSItem.builder().url("tsenyurt.com").title("Test Title").description("desc")
          .publication(new Date())
          .build();

      repository.save(item);
      */

      /*rssDataService.readFromExternalFeed();*/

      businessService.updateRssFeedsInDb();
    };
  }
}
