package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.article.ArticleComment;
import com.ezhixuan.blog.service.ArticleCommentService;
import com.ezhixuan.blog.mapper.ArticleCommentMapper;
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




