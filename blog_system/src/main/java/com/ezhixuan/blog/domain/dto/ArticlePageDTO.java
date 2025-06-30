package com.ezhixuan.blog.domain.dto;

import java.util.Date;

import lombok.Data;

/**
 * 文章分页 DTO
 */
@Data
public class ArticlePageDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 作者ID
     */
    private Long userId;

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
     * 标签id
     */
    private String tagIds;

    /**
     * 文章字数
     */
    private Integer wordCount;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 状态：1-已发布，0-草稿
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
