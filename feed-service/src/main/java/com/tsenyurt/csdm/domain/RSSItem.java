package com.tsenyurt.csdm.domain;

import com.tsenyurt.csdm.view.RssItemView;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "rss_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "url")
@ToString(of = {"url", "title"})
public class RSSItem implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rssitem_seq_gen")
  @SequenceGenerator(name = "rssitem_seq_gen", sequenceName = "RSS_ITEM_SEQ", allocationSize = 1)
  private Long id;

  private String url;

  private String title;

  private String description;

  @Temporal(TemporalType.TIMESTAMP)
  private Date publication;

  @Temporal(TemporalType.TIMESTAMP)
  private Date updateTime;

  private String imageUrl;

  public static RssItemView convertToView(RSSItem it) {
    if(it == null)
      return null;
    return RssItemView.builder()
        .id(it.getId())
        .url(it.getUrl())
        .title(it.getTitle())
        .description(it.getDescription())
        .publication(it.getPublication())
        .updateTime(it.getUpdateTime())
        .imageUrl(it.getImageUrl())
        .build();
  }
}
