package com.ezhixuan.blog.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.domain.entity.article.ArticleTag;
import com.ezhixuan.blog.mapper.ArticleTagMapper;
import com.ezhixuan.blog.service.ArticleTagService;

/**
* @author ezhixuan
* @description 针对表【article_tag(标签表)】的数据库操作Service实现
* @createDate 2025-03-27 09:45:58
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

    private static final String DEFAULT_TAG = "默认";

    /**
     * 获取默认 tag Id
     *
     * @return id
     * @author Ezhixuan
     */
    @Override
    @Cache
    public Long getDefaultId() {
        ArticleTag defaultTag = getOne(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getName,(DEFAULT_TAG)));
        if (Objects.isNull(defaultTag)) {
            defaultTag = new ArticleTag();
            defaultTag.setName(DEFAULT_TAG);
            save(defaultTag);
        }
        return defaultTag.getId();
    }
}




