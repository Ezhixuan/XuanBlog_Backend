package com.ezhixuan.blog.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.common.PageRequest;
import com.ezhixuan.blog.domain.dto.ArticlePageDTO;
import com.ezhixuan.blog.domain.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.entity.article.Article;
import com.ezhixuan.blog.mapper.ArticleMapper;
import com.ezhixuan.blog.service.ArticleService;

import cn.hutool.core.bean.BeanUtil;

/**
 * @author ezhixuan
 * @description 针对表【article(文章表)】的数据库操作Service实现
 * @createDate 2025-03-27 09:45:21
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public IPage<ArticlePageDTO> getArticlePageList(ArticleQueryDTO articleQueryDTO) {
        LambdaQueryWrapper<Article> qw = queryWrapper(articleQueryDTO);
        IPage<Article> iPage = articleQueryDTO.toIPage();
        page(iPage, qw);
        return PageRequest.convert(iPage, item -> BeanUtil.copyProperties(item, ArticlePageDTO.class));
    }

    @Override
    public ArticlePageDTO getArticleById(Long id) {
        Article article = getById(id);
        return BeanUtil.copyProperties(article, ArticlePageDTO.class);
    }

    private LambdaQueryWrapper<Article> queryWrapper(ArticleQueryDTO queryDTO) {
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();

        qw.like(Objects.nonNull(queryDTO.getTitle()), Article::getTitle, queryDTO.getTitle());
        qw.like(Objects.nonNull(queryDTO.getSummary()), Article::getSummary, queryDTO.getSummary());
        qw.in(!ObjectUtils.isEmpty(queryDTO.getCategoryIds()), Article::getCategoryId, queryDTO.getCategoryIds());
        if (!ObjectUtils.isEmpty(queryDTO.getTagIds())) {
            StringBuilder tagIdsStr = new StringBuilder();
            boolean isFirst = true;
            for (Long tagId : queryDTO.getTagIds()) {
                if (!isFirst) {
                    tagIdsStr.append(",");
                }
                tagIdsStr.append(tagId);
                isFirst = false;
            }
            qw.and(wrapper -> {
                for (String tagId : tagIdsStr.toString().split(",")) {
                    wrapper.or().apply("FIND_IN_SET({0}, tag_ids)", tagId);
                }
            });
        }
        qw.in(!ObjectUtils.isEmpty(queryDTO.getIds()), Article::getId, queryDTO.getIds());
        qw.orderBy(Objects.nonNull(queryDTO.getSortOrder()), !Objects.equals(queryDTO.getSortOrder(), "descend"), Article::getCreateTime);

        return qw;
    }
}
