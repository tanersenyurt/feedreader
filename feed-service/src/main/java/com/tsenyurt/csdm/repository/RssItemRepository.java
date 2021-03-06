package com.tsenyurt.csdm.repository;

import com.tsenyurt.csdm.domain.RSSItem;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RssItemRepository extends JpaRepository<RSSItem, Long> {
  @Query("select count(r) from RSSItem r")
  Long countAllRecords();

  RSSItem findByUrl(String url);

  @Query("select r from RSSItem r where r.url in ( :urls )")
  List<RSSItem> findByUrlList(@Param("urls") List<String> urls);

  @Transactional
  @Modifying
  @Query(
      value =
          "delete from RSS.RSS_ITEM r where r.publication = (select  min(x.publication) from RSS.RSS_ITEM x )",
      nativeQuery = true)
  int removeOldestRecord();
}
