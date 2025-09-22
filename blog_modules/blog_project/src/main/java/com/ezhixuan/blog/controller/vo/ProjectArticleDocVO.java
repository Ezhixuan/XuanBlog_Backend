package com.ezhixuan.blog.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 项目文章 Vo
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Data
public class ProjectArticleDocVO {

    @Schema(description = "文章 id")
    private Long id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "排序")
    private Integer sortOrder;
}
