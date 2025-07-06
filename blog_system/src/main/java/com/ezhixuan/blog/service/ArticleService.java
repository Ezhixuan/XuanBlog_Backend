package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.dto.ArticlePageDTO;
import com.ezhixuan.blog.domain.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.entity.article.Article;

/**
* @author ezhixuan
* @description 针对表【article(文章表)】的数据库操作Service
* @createDate 2025-03-27 09:45:21
*/
public interface ArticleService extends IService<Article> {

    /**
     * 分页查询文章列表
     * @author Ezhixuan
     * @param articlePageDTO 查询参数
     * @return IPage<ArticlePageDTO>
     */
    IPage<ArticlePageDTO> getArticlePageList(ArticleQueryDTO articlePageDTO);

    /**
     * 根据 id 获取文章信息
     * @author Ezhixuan
     * @param id 文章id
     * @return ArticlePageDTO
     */
    ArticlePageDTO getArticleById(Long id);

}
