package com.ezhixuan.blog.domain.entity.article;

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 分类表
 * @TableName article_category
 */
@TableName(value ="article_category")
@Data
public class ArticleCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 5449958369178159577L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    @TableField(value = "name")
    private String name;
}
