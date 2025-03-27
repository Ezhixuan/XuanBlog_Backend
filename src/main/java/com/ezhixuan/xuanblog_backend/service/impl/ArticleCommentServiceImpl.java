package com.ezhixuan.xuanblog_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.xuanblog_backend.domain.entity.article.ArticleComment;
import com.ezhixuan.xuanblog_backend.service.ArticleCommentService;
import com.ezhixuan.xuanblog_backend.mapper.ArticleCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【article_comment(评论表)】的数据库操作Service实现
* @createDate 2025-03-27 09:45:47
*/
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment>
    implements ArticleCommentService{

}




