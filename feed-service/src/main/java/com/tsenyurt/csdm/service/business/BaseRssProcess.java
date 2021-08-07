package com.tsenyurt.csdm.service.business;

import com.tsenyurt.csdm.domain.*;
import com.tsenyurt.csdm.view.*;

public interface BaseRssProcess {
  public void processRssItem(RssItemView rssItemView) ;
  public void updateRssItemInfos(RssItemView rssItemView, RSSItem item);
}
