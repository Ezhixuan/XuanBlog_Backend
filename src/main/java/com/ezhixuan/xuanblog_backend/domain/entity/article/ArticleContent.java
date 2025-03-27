package com.ezhixuan.xuanblog_backend.domain.entity.article;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 文章内容
 * @TableName article_content
 */
@TableName(value ="article_content")
@Data
public class ArticleContent implements Serializable {
    /**
     * 文章id
     */
    @TableId(value = "article_id", type = IdType.INPUT)
    private Long articleId;

    /**
     * 文章内容
     */
    @TableField(value = "content")
    private String content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
