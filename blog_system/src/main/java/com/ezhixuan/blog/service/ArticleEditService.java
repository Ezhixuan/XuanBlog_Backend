package com.ezhixuan.blog.service;

import com.ezhixuan.blog.domain.dto.ArticleSubmitDTO;

public interface ArticleEditService {

    /**
     * 上传博客
     * @author Ezhixuan
     * @param articleSubmitDTO 提交dto
     */
    void doSubmitArticle(ArticleSubmitDTO articleSubmitDTO);
}
