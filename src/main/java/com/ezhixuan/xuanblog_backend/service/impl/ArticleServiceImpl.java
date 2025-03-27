package com.ezhixuan.xuanblog_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.domain.entity.article.Article;
import com.ezhixuan.xuanblog_backend.service.ArticleService;
import com.ezhixuan.xuanblog_backend.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【article(文章表)】的数据库操作Service实现
* @createDate 2025-03-27 09:45:21
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

}




