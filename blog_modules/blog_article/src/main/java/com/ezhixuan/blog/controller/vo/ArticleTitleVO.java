package com.ezhixuan.blog.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章名与 id VO
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Data
public class ArticleTitleVO {

    @Schema(description = "文章id")
    private Long id;

    @Schema(description = "文章名")
    private String title;

    public static ArticleTitleVO of(Long id, String title) {
        ArticleTitleVO vo = new ArticleTitleVO();
        vo.setId(id);
        vo.setTitle(title);
        return vo;
    }
}
