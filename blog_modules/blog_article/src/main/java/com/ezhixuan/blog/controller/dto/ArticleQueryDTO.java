package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "文章分页 DTO")
public class ArticleQueryDTO extends PageRequest {

  @Schema(description = "文章标题")
  private String title;

  @Schema(description = "文章摘要")
  private String summary;

  @Schema(description = "分类名称")
  private String categoryName;

  @Schema(description = "标签名")
  private String tagName;

  @Schema(description = "根据id查询")
  private Collection<Long> ids = new ArrayList<>();

  @Schema(description = "标签id")
  private Collection<Long> tagIds;

  @Schema(description = "菜单id")
  private Collection<Long> categoryIds;

  @Schema(description = "项目 id")
  private Long projectId;

  @Schema(description = "是否需要封面")
  private Boolean needCover;

  public List<Long> getTagIds() {
    if (isEmpty(tagIds)) {
      return List.of();
    }
    return tagIds.stream().distinct().toList();
  }

  public List<Long> getCategoryIds() {
    if (isEmpty(categoryIds)) {
      return List.of();
    }
    return categoryIds.stream().distinct().toList();
  }

  public List<Long> getIds() {
    if (isEmpty(ids)) {
      return List.of();
    }
    return ids.stream().distinct().toList();
  }
}
