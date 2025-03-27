package com.ezhixuan.xuanblog_backend.domain.dto;

import lombok.Data;

/**
 * 文章提交 dto
 */
@Data
public class ArticleSubmitDTO {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 封面图片
     */
    private String cover;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 标签id列表
     */
    private String tagIds;

    /**
     * 状态：1-已发布，0-草稿
     */
    private Integer status;
}
