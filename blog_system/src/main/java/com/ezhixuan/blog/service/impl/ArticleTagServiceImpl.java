package com.ezhixuan.blog.service.impl;

import java.util.Collection;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
     * 通过tag名模糊搜索符合的标签id
     *
     * @param tagName 标签名
     * @return ids
     */
    public Collection<Long> getIdsByTagName(String tagName) {
        LambdaQueryWrapper<ArticleTag> qw = queryWrapper(tagName);
        qw.select(ArticleTag::getId);
        return listObjs(qw, o -> (long)o);
    }

    /**
     * 获取默认 tag Id
     *
     * @return id
     * @author Ezhixuan
     */
    @Override
    public Long getDefaultId() {
        ArticleTag defaultTag = getOne(queryWrapper(DEFAULT_TAG));
        if (Objects.isNull(defaultTag)) {
            defaultTag = new ArticleTag();
            defaultTag.setName(DEFAULT_TAG);
            save(defaultTag);
        }
        return defaultTag.getId();
    }

    /**
     * 内部方法
     * 本服务只提供根据名称模糊搜索
     * @param tagName 标签名
     * @return qw
     */
    private LambdaQueryWrapper<ArticleTag> queryWrapper(String tagName) {
        LambdaQueryWrapper<ArticleTag> qw = new LambdaQueryWrapper<>();

        qw.like(ArticleTag::getName, tagName);

        return qw;
    }
}




