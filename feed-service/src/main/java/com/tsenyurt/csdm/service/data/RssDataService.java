package com.tsenyurt.csdm.service.data;

import com.tsenyurt.csdm.service.data.impl.exception.*;
import com.tsenyurt.csdm.view.RssItemView;
import java.util.List;

public interface RssDataService {
  public List<RssItemView> readFromExternalFeed() throws FeedReadException;

  public List<RssItemView> getLatestRssFeedsFromExternalSource();
}
