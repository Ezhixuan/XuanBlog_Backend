package com.ezhixuan.xuanblog_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleTag;
import com.ezhixuan.xuanblog_backend.service.ArticleTagService;
import com.ezhixuan.xuanblog_backend.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【article_tag(标签表)】的数据库操作Service实现
* @createDate 2025-03-27 09:45:58
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




