package com.ezhixuan.xuanblog_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticlePageDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticlePageVO;

import java.util.Collection;
import java.util.List;

/**
 * 文章查询
 */
public interface ArticleQueryService {

    IPage<ArticlePageVO> getArticlePageVOList(ArticleQueryDTO articleQueryDTO);

    ArticlePageVO toPageVO(ArticlePageDTO articlePageDTO);

    List<ArticlePageVO> toPageVOList(Collection<ArticlePageDTO> collection);
}
