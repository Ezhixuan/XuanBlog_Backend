package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.annotation.Cache;
import com.ezhixuan.blog.entity.ArticleTag;
import com.ezhixuan.blog.mapper.ArticleTagMapper;
import com.ezhixuan.blog.service.ArticleTagService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 文章标签服务实现类
 *
 * @author Ezhixuan
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

    private static final String DEFAULT_TAG = "默认";

    /**
     * 获取默认标签ID，如果不存在则创建默认标签
     *
     * @return Long 默认标签ID
     */
    @Override
    @Cache
    public Long getDefaultId() {
        // 查询默认标签
        ArticleTag defaultTag = getOne(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getName,(DEFAULT_TAG)));
        // 如果默认标签不存在，则创建一个默认标签
        if (Objects.isNull(defaultTag)) {
            defaultTag = new ArticleTag();
            defaultTag.setName(DEFAULT_TAG);
            save(defaultTag);
        }
        return defaultTag.getId();
    }
}




