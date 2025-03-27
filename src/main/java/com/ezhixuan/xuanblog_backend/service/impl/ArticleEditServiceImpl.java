package com.ezhixuan.xuanblog_backend.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.ezhixuan.xuanblog_backend.domain.dto.ArticleSubmitDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.article.Article;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.ArticleEditService;
import com.ezhixuan.xuanblog_backend.service.ArticleService;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleEditServiceImpl implements ArticleEditService {

    final ArticleService articleService;

    /**
     * 上传博客
     *
     * @param articleSubmitDTO 提交dto
     * @author Ezhixuan
     */
    @Override
    public void doSubmitArticle(ArticleSubmitDTO articleSubmitDTO) {
        /*
         todo Ezhixuan : 1. 根据文章内容生成摘要
                         2. 提供根据文章标题自动搜索合适封面的选项
                         3. 字数计数
                         4. 内容审核
                         5. 支持将图片上传图床
                         6. 通过md文件上传文章
         */
        ThrowUtils.throwIf(StrUtil.isBlank(articleSubmitDTO.getTitle()), ErrorCode.PARAMS_ERROR, "博客标题不能为空");
        if (StrUtil.isBlank(articleSubmitDTO.getTagIds())) {
            articleSubmitDTO.setTagIds("1");
        }
        if (Objects.isNull(articleSubmitDTO.getCategoryId())) {
            articleSubmitDTO.setCategoryId(1L);
        }
        Article article = BeanUtil.copyProperties(articleSubmitDTO, Article.class);
        article.setUserId(StpUtil.getLoginIdAsLong());
        articleService.save(article);
    }
}
