package com.ezhixuan.xuanblog_backend.service;

import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
* @author ezhixuan
* @description 针对表【article_tag(标签表)】的数据库操作Service
* @createDate 2025-03-27 09:45:58
*/
public interface ArticleTagService extends IService<ArticleTag> {

    /**
     * 通过tag名模糊搜索符合的id
     * @param tagName 标签名
     * @return ids
     */
    Collection<Long> getIdsByTagName(String tagName);
}
