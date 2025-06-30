package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.article.ArticleContent;
import com.ezhixuan.blog.service.ArticleContentService;
import com.ezhixuan.blog.mapper.ArticleContentMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【article_content(文章内容)】的数据库操作Service实现
* @createDate 2025-03-27 09:57:40
*/
@Service
public class ArticleContentServiceImpl extends ServiceImpl<ArticleContentMapper, ArticleContent>
    implements ArticleContentService{

}




