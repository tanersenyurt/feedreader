package com.tsenyurt.csdm.service.data;

import com.tsenyurt.csdm.view.RssItemView;
import java.util.List;

public interface RssDataService {
  public List<RssItemView> readFromExternalFeed() throws Exception;
  public List<RssItemView> getLatestRssFeedsFromExternalSource();
}
