package com.ezhixuan.xuanblog_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleCategory;
import com.ezhixuan.xuanblog_backend.service.ArticleCategoryService;
import com.ezhixuan.xuanblog_backend.mapper.ArticleCategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【article_category(分类表)】的数据库操作Service实现
* @createDate 2025-03-27 09:45:26
*/
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory>
    implements ArticleCategoryService{

}




