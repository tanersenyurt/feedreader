package com.tsenyurt.csdm.view;

import java.io.*;
import java.util.*;

import com.rometools.rome.feed.synd.*;
import com.tsenyurt.csdm.domain.*;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(of = {"url", "id"})
@ToString(of = {"url", "title", "publication"})
public class RssItemView implements Serializable {
  private Long id;
  private String url;
  private String title;
  private String description;
  private Date publication;
  private Date updateTime;
  private String imageUrl;

  public static RssItemView createInstance(SyndEntry item) {
    String imageUri = "";
    StringJoiner joiner = new StringJoiner(";");
    item.getEnclosures().forEach(uri -> joiner.add(uri.getUrl()));
    if (!item.getEnclosures().isEmpty()) {
      imageUri = joiner.toString();
    }

    return RssItemView.builder()
        .url(item.getUri())
        .title(item.getTitle())
        .description(item.getDescription() == null ? "" : item.getDescription().getValue())
        .publication(item.getPublishedDate())
        .updateTime(item.getUpdatedDate())
        .imageUrl(imageUri)
        .build();
  }

  public static RSSItem createEntity(RssItemView item) {
    return RSSItem.builder()
        .url(item.getUrl())
        .title(item.getTitle())
        .description(item.getDescription())
        .publication(item.getPublication())
        .updateTime(item.getUpdateTime())
        .imageUrl(item.getImageUrl())
        .build();
  }
}
