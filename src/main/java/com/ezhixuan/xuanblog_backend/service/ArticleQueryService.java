package com.ezhixuan.xuanblog_backend.service;

import java.util.Collection;
import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticlePageDTO;
import com.ezhixuan.xuanblog_backend.domain.dto.ArticleQueryDTO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleCategoryCountVO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleInfoVO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticlePageVO;
import com.ezhixuan.xuanblog_backend.domain.vo.ArticleTagCountVO;

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
    IPage<ArticlePageVO> getArticlePageVOList(ArticleQueryDTO articleQueryDTO);

    /**
     * 查询结果DTO 转 VO
     * @author Ezhixuan
     * @param articlePageDTO 需要包含article数据
     * @return ArticlePageVO 脱敏后数据
     */
    ArticlePageVO toPageVO(ArticlePageDTO articlePageDTO);

    /**
     * 转Vo
     * @author Ezhixuan
     * @param collection 注意articlePageDTO需要包含article数据
     * @return 脱敏后List数据
     */
    List<ArticlePageVO> toPageVOList(Collection<ArticlePageDTO> collection);

    /**
     * 菜单数量使用情况统计
     * @author Ezhixuan
     * @return 统计列表
     */
    List<ArticleCategoryCountVO> getCategoryCount();

    /**
     * 标签数量使用情况统计
     * @author Ezhixuan
     * @return 统计列表
     */
    List<ArticleTagCountVO> getTagCount();

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

    /**
     * 更新 viewCount
     * @author Ezhixuan
     * @param articleId 文章 id
     * @param viewCount 数量
     */
    @Async
    void asyncUpdateViewCount(long articleId, Integer viewCount);
}
