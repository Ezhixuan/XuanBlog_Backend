package com.ezhixuan.xuanblog_backend.domain.entity.article;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 文章标签关联表
 * @TableName article_tag_link
 */
@TableName(value ="article_tag_link")
@Data
public class ArticleTagLink implements Serializable {

    /**
     * 文章ID
     */
    @TableId(value = "article_id", type = IdType.INPUT)
    private Long articleId;

    /**
     * 标签ID
     */
    @TableField(value = "tag_id")
    private Long tagId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
