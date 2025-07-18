package com.ezhixuan.blog.domain.entity.article;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ArticleThumb {
    @Schema(description="主键 id")
    private Long id;
    @Schema(description="用户 id")
    @TableField("user_id")
    private Long userId;
    @Schema(description="文章 id")
    @TableField("article_id")
    private Long articleId;
}
