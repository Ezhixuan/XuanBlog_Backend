package com.ezhixuan.blog.domain.constant;

/**
 * 部分内部 key
 */
public interface RedisKeyConstant {

    String BLOG_PREFIX = "blog:";

    /* article */
    String ARTICLE_INFO_PRE_KEY = BLOG_PREFIX + "article:info:";
    String ARTICLE_THUMB_PRE_KEY = BLOG_PREFIX + "article:thumb:";
    String ARTICLE_THUMB_TEMP_PRE_KEY = BLOG_PREFIX + "article:thumb:temp:";
    String LIST_TAG_KEY = BLOG_PREFIX + "list:tag";
    String LIST_CATEGORY_KEY = BLOG_PREFIX + "list:category";
}
