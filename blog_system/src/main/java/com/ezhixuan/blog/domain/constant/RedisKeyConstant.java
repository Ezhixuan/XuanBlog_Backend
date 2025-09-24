package com.ezhixuan.blog.domain.constant;

/** 部分内部 key */
public interface RedisKeyConstant {

  String BLOG_PREFIX = "blog:";

  /* article */
  String ARTICLE_INFO_PRE_KEY = BLOG_PREFIX + "article:info:";
  String ARTICLE_THUMB_PRE_KEY = BLOG_PREFIX + "article:thumb:";
  String ARTICLE_THUMB_TEMP_PRE_KEY = BLOG_PREFIX + "temp:article:thumb:";
  String LIST_TAG_KEY = BLOG_PREFIX + "list:tag";
  String LIST_CATEGORY_KEY = BLOG_PREFIX + "list:category";
  String COUNT_CATEGORY_KEY = BLOG_PREFIX + "count:category";
  String COUNT_TAG_KEY = BLOG_PREFIX + "count:tag";
  String DEFAULT_CATEGORY_ID_KEY = BLOG_PREFIX + "default:category";
  String ARTICLE_RECOMMEND_LIST_KEY = BLOG_PREFIX + "recommend";

  /* user */
  String USER_INFO_KEY = BLOG_PREFIX + "user:info";
  String ADMIN_INFO_KEY = BLOG_PREFIX + "user:info:admin";
}
