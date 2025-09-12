package com.ezhixuan.blog.controller.dto;

import com.ezhixuan.blog.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 文章分页 DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleQueryDTO extends PageRequest {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 根据id查询
     */
    private Collection<Long> ids = new ArrayList<>();

    /**
     * 标签id
     */
    private Collection<Long> tagIds;

    /**
     * 菜单id
     */
    private Collection<Long> categoryIds;

    /**
     * 项目 id
     */
    private Long projectId;

    public List<Long> getTagIds() {
        if (CollectionUtils.isEmpty(tagIds)) {
            return Collections.emptyList();
        }
        return tagIds.stream().distinct().toList();
    }

    public List<Long> getCategoryIds() {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return Collections.emptyList();
        }
        return categoryIds.stream().distinct().toList();
    }
}
