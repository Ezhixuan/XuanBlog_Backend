package com.ezhixuan.xuanblog_backend.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章详情
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleInfoVO extends ArticlePageVO{

    /**
     * 文章内容
     */
    private String content;
}
