package com.ezhixuan.xuanblog_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticlePageDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.entity.article.Article;

/**
* @author ezhixuan
* @description 针对表【article(文章表)】的数据库操作Service
* @createDate 2025-03-27 09:45:21
*/
public interface ArticleService extends IService<Article> {

    IPage<ArticlePageDTO> getArticlePageList(ArticleQueryDTO articlePageDTO);

}
