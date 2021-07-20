package com.tsenyurt.csdm.service;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Getter;

public enum ProcessType {
  SYNC("syncRssProcessService"),
  ASYNC("asyncRssProcessService");

  @Getter private final String bean;

  ProcessType(String bean) {
    this.bean = bean;
  }

  private static final Map<String, ProcessType> _nameMap = Maps.newHashMap();

  static {
    for (ProcessType type : ProcessType.values()) {
      _nameMap.put(type.name(), type);
    }
  }

  public static ProcessType findFromName(String name) {
    return _nameMap.get(name);
  }
}
