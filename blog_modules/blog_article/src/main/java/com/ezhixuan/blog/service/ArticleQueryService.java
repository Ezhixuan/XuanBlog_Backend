package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.controller.dto.ArticleQueryDTO;
import com.ezhixuan.blog.controller.vo.ArticleInfoVO;
import com.ezhixuan.blog.controller.vo.ArticlePageVO;
import com.ezhixuan.blog.controller.vo.CountVO;
import java.util.List;

/**
 * 文章查询统一接口
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
public interface ArticleQueryService {

  /**
   * 分页查询
   *
   * @param articleQueryDTO 查询条件
   * @return 包含vo数据
   */
  IPage<ArticlePageVO> pageArticleListByDTO(ArticleQueryDTO articleQueryDTO);

  /**
   * 获取文章详情
   *
   * @param articleId 文章id
   * @return ArticleInfoVO
   */
  ArticleInfoVO getArticleCache(long articleId);

  /**
   * 查询文章详情
   *
   * @param articleId 文章id
   * @return 文章内容vo
   */
  ArticleInfoVO getArticleVO(long articleId);

  /**
   * 分类统计
   *
   * @param num 统计数量
   * @return 分类统计
   */
  List<CountVO> getCategoryCountVo(int num);

  /**
   * 标签统计
   *
   * @param num 统计数量
   * @return 标签统计
   */
  List<CountVO> getTagCountVo(int num);

  /**
   * 获取推荐文章
   *
   * @param num 获取数量
   * @return 推荐文章
   */
  List<ArticlePageVO> getRecommendedList(int num);
}
