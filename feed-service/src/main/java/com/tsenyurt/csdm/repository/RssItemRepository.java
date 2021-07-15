package com.tsenyurt.csdm.repository;

import com.tsenyurt.csdm.domain.RSSItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RssItemRepository  extends JpaRepository<RSSItem, Long> {

}
