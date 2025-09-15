package com.ezhixuan.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.controller.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.dto.CountDto;
import com.ezhixuan.blog.domain.entity.Article;
import com.ezhixuan.blog.mapper.ArticleMapper;
import com.ezhixuan.blog.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author ezhixuan
 * @description 针对表【article(文章主表)】的数据库操作Service实现
 * @createDate 2025-09-13 10:40:23
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

  /**
   * 分页查询
   *
   * @param articleQueryDTO 查询条件
   * @return 分页数据
   */
  @Override
  public IPage<Article> page(ArticleQueryDTO articleQueryDTO) {
    if (isNull(articleQueryDTO)) {
      return new Page<>();
    }
    return page(articleQueryDTO.toPage(), getQueryWrapper(articleQueryDTO));
  }

  /**
   * 获取文章分类使用数量
   *
   * @param num 数量
   * @return 分类使用数量
   */
  @Override
  public Map<Long, Long> getCategoryUseCount(int num) {
    Map<Long, CountDto> longCountDtoMap = baseMapper.selectCategoryUseCount(num);
    return longCountDtoMap.entrySet().stream()
        .collect(
            java.util.stream.Collectors.toMap(
                Map.Entry::getKey, entry -> entry.getValue().getCount()));
  }

  private Wrapper<Article> getQueryWrapper(ArticleQueryDTO articleQueryDTO) {
    LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
    try {
      long loginUserId = StpUtil.getLoginIdAsLong();
      if (!Objects.equals(loginUserId, 1L)) {
        lqw.eq(Article::getStatus, 1);
      }
    } catch (Exception ignored) {
    }
    List<Long> categoryIds = articleQueryDTO.getCategoryIds();
    List<Long> ids = articleQueryDTO.getIds();

    return Wrappers.<Article>lambdaQuery()
        .eq(
            nonNull(articleQueryDTO.getProjectId()),
            Article::getProjectId,
            articleQueryDTO.getProjectId())
        .in(!categoryIds.isEmpty(), Article::getCategoryId, categoryIds)
        .in(!ids.isEmpty(), Article::getId, ids)
        .like(nonNull(articleQueryDTO.getTitle()), Article::getTitle, articleQueryDTO.getTitle())
        .like(
            nonNull(articleQueryDTO.getSummary()),
            Article::getSummary,
            articleQueryDTO.getSummary());
  }

  /**
   * 判断项目下是否有文章
   *
   * @param projectId 项目id
   * @return 是否有文章
   */
  @Override
  public boolean hasArticle(Long projectId) {
    if (isNull(projectId)) {
      return false;
    }
    return count(new LambdaQueryWrapper<Article>().eq(Article::getProjectId, projectId)) > 0;
  }
}
