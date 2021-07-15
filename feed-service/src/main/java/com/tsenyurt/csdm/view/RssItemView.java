package com.tsenyurt.csdm.view;

import com.rometools.rome.feed.synd.SyndEntry;
import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(of = {"url","id"})
@ToString(of = {"url", "title" , "publication"})
public class RssItemView implements Serializable {
  private Long id;
  private String url;
  private String title;
  private String description;
  private Date publication;
  private Date updateTime;
  private String imageUrl;

  public static RssItemView createInstance(SyndEntry item)
  {
    return  RssItemView.builder().
        url(item.getUri()).
        title(item.getTitle()).
        description(item.getDescription() ==null?"":item.getDescription().getValue()).
        publication(item.getPublishedDate()).
        updateTime(item.getUpdatedDate()).
        imageUrl(item.getEnclosures().get(0).getUrl()) //TODO:Handle none
        .build();
  }
}
