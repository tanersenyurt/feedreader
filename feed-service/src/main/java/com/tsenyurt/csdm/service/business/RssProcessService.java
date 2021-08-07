package com.tsenyurt.csdm.service.business;

import com.tsenyurt.csdm.view.RssItemView;
import java.util.List;

public interface RssProcessService {
  int ZERO_INT = 0;

  public void process(List<RssItemView> rssItemViewList);
}
