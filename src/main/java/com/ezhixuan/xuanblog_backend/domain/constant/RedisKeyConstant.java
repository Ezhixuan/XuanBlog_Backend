package com.ezhixuan.xuanblog_backend.domain.constant;

/**
 * 部分内部 key
 */
public interface RedisKeyConstant {

    String BLOG_PREFIX = "blog:";

    /* article */
    String ARTICLE_INFO_PRE_KEY = BLOG_PREFIX + "article:info:";
    String ARTICLE_THUMB_PRE_KEY = BLOG_PREFIX + "article:thumb:";
    String ARTICLE_THUMB_TEMP_PRE_KEY = BLOG_PREFIX + "article:thumb:temp:";
}
