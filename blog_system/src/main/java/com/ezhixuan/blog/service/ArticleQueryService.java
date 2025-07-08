package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.domain.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.vo.ArticleInfoVO;
import com.ezhixuan.blog.domain.vo.ArticlePageVO;


/**
 * 文章查询
 */
public interface ArticleQueryService {

    /**
     * 分页查询
     * @author Ezhixuan
     * @param articleQueryDTO 查询条件
     * @return 包含vo数据
     */
    IPage<ArticlePageVO> pageListByDTO(ArticleQueryDTO articleQueryDTO);

    /**
     * 获取文章详情
     * @author Ezhixuan
     * @param articleId
     * @return ArticleInfoVO
     */
    ArticleInfoVO getArticleInfo(long articleId);

    /**
     * 查询文章详情
     * @author Ezhixuan
     * @param articleId 文章id
     * @return 文章内容vo
     */
    ArticleInfoVO getArticleInfoVO(long articleId);
}
