package com.tsenyurt.csdm.repository;

import com.tsenyurt.csdm.domain.RSSItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RssItemRepository  extends JpaRepository<RSSItem, Long> {
  @Query("select count(r) from RSSItem r")
  Long countAllRecords();

  RSSItem findByUrl(String url);

  @Query("select r from RSSItem r where r.url in ( :urls )")
  List<RSSItem> findByUrlList(List<String> urls);
}
