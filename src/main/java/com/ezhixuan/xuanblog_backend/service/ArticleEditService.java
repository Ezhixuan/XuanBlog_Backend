package com.ezhixuan.xuanblog_backend.service;

import com.ezhixuan.xuanblog_backend.domain.dto.ArticleSubmitDTO;

public interface ArticleEditService {

    /**
     * 上传博客
     * @author Ezhixuan
     * @param articleSubmitDTO 提交dto
     */
    void doSubmitArticle(ArticleSubmitDTO articleSubmitDTO);
}
