package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.article.ArticleTag;

/**
* @author ezhixuan
* @description 针对表【article_tag(标签表)】的数据库操作Service
* @createDate 2025-03-27 09:45:58
*/
public interface ArticleTagService extends IService<ArticleTag> {

    /**
     * 获取默认 tag Id
     * @author Ezhixuan
     * @return id
     */
    Long getDefaultId();
}
