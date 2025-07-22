package com.ezhixuan.blog.domain.entity.article;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 文章表
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目 id
     */
    @TableField(value = "project_id")
    private Long projectId;

    /**
     * 文章标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 作者ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 文章摘要
     */
    @TableField(value = "summary")
    private String summary;

    /**
     * 封面图片
     */
    @TableField(value = "cover")
    private String cover;

    /**
     * 文章字数
     */
    @TableField(value = "word_count")
    private Integer wordCount;

    /**
     * 浏览量
     */
    @TableField(value = "view_count")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @TableField(value = "like_count")
    private Integer likeCount;

    /**
     * 评论数
     */
    @TableField(value = "comment_count")
    private Integer commentCount;

    /**
     * 状态：1-已发布，0-草稿
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField(value = "deleted")
    private Integer deleted;

    @Serial
    private static final long serialVersionUID = 1L;
}
