package com.tsenyurt.csdm.service.data;

import com.tsenyurt.csdm.domain.RSSItem;
import java.util.List;

public interface RssDataService {
  public List<RSSItem> readFromExternalFeed() throws Exception;

}
