package com.ezhixuan.blog.service.impl;

import java.util.Collection;
import java.util.List;

import com.ezhixuan.blog.domain.vo.CountVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.controller.article.LinkArticleTag;
import com.ezhixuan.blog.mapper.LinkArticleTagMapper;
import com.ezhixuan.blog.service.LinkArticleTagService;

import lombok.RequiredArgsConstructor;

/**
* @author ezhixuan
* @description 针对表【link_article_tag(关联 文章与标签)】的数据库操作Service实现
* @createDate 2025-07-06 14:59:36
*/
@Service
@RequiredArgsConstructor
public class LinkArticleTagServiceImpl extends ServiceImpl<LinkArticleTagMapper, LinkArticleTag>
    implements LinkArticleTagService{

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(Long articleId, List<Long> tagIds) {
        List<LinkArticleTag> list = list(Wrappers.<LinkArticleTag>lambdaQuery().eq(LinkArticleTag::getArticleId, articleId).in(LinkArticleTag::getTagId, tagIds));
        if (!CollectionUtils.isEmpty(list)) {
            removeBatchByIds(list);
        }
        tagIds.forEach(tagId -> {
            LinkArticleTag link = new LinkArticleTag();
            link.setArticleId(articleId);
            link.setTagId(tagId);
            save(link);
        });
    }

    /**
     * 获取关联的文章 id
     *
     * @param tagIds 标签id
     * @return Collection<Long>
     * @author Ezhixuan
     */
    @Override
    public Collection<Long> queryArticleId(Collection<Long> tagIds) {
        return listObjs(Wrappers.<LinkArticleTag>lambdaQuery().select(LinkArticleTag::getArticleId).in(LinkArticleTag::getTagId, tagIds));
    }

    /**
     * 获取标签计数
     *
     * @return List<ArticleTagCountVO>
     * @author Ezhixuan
     */
    @Override
    public List<CountVO> getTagCount() {
        return baseMapper.getTagCount();
    }

    /**
     * 获取文章关联的标签 id
     *
     * @param articleIds 文章id
     * @return Collection<Long>
     * @author Ezhixuan
     */
    @Override
    public Collection<Long> queryTagId(List<Long> articleIds) {
        return listObjs(Wrappers.<LinkArticleTag>lambdaQuery().select(LinkArticleTag::getTagId).in(LinkArticleTag::getArticleId, articleIds));
    }

    /**
     * 获取文章关联的标签
     *
     * @param articleIds 文章id
     * @return List<LinkArticleTag>
     * @author Ezhixuan
     */
    @Override
    public List<LinkArticleTag> queryLink(List<Long> articleIds) {
        return list(Wrappers.<LinkArticleTag>lambdaQuery().in(LinkArticleTag::getArticleId, articleIds));
    }

    /**
     * 通过 articleId 断开连接
     *
     * @param articleId 文章 id
     * @author Ezhixuan
     */
    @Override
    public void removeByArticle(Long articleId) {
        remove(Wrappers.<LinkArticleTag>lambdaQuery().eq(LinkArticleTag::getArticleId, articleId));
    }
}




