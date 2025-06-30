package com.ezhixuan.blog.domain.entity.article;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 评论表
 * @TableName article_comment
 */
@TableName(value ="article_comment")
@Data
public class ArticleComment implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * 评论用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 父评论ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 回复用户ID
     */
    @TableField(value = "to_user_id")
    private Long toUserId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
