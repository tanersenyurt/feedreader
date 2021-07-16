package com.tsenyurt.csdm.service.business;

import com.tsenyurt.csdm.view.RssItemView;
import java.util.function.Function;

public interface RssBusinessService {
  int ZERO_INT = 0;
  public static Function<RssItemView, String> rssItemToUrl = item -> item.getUrl();

  public void updateRssFeedsInDb();

  public void startAsyncQueue();
}
