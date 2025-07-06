package com.ezhixuan.blog.controller.article;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LinkArticleCategory {

    @Schema(description="主键 id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description="文章 id")
    @TableField("article_id")
    private Long articleId;

    @Schema(description="分类 id")
    @TableField("category_id")
    private Long categoryId;
}
