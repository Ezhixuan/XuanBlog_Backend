package com.ezhixuan.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.dto.ArticleQueryDTO;
import com.ezhixuan.blog.domain.entity.article.Article;
import com.ezhixuan.blog.mapper.ArticleMapper;
import com.ezhixuan.blog.service.ArticleService;
import com.ezhixuan.blog.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * 文章服务实现类
 *
 * @author Ezhixuan
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

  private final SysUserService sysUserService;

  /**
   * 分页查询文章列表
   *
   * @param articleQueryDTO 查询参数
   * @return IPage<Article> 分页结果
   */
  @Override
  public IPage<Article> pageList(ArticleQueryDTO articleQueryDTO) {
    LambdaQueryWrapper<Article> qw = queryWrapper(articleQueryDTO);
    qw.eq(Article::getProjectId, articleQueryDTO.getProjectId());
    IPage<Article> iPage = articleQueryDTO.toPage();
    page(iPage, qw);
    return iPage;
  }

  /**
   * 根据 id 获取文章信息
   *
   * @param id 文章id
   * @return Article 文章实体
   */
  @Override
  public Article getArticleById(Long id) {
    return getById(id);
  }

  /**
   * 判断项目是否有项目文档
   *
   * @param projectId 项目id
   * @return boolean
   */
  @Override
  public boolean hasArticle(Long projectId) {
    if (isNull(projectId)) {
      return false;
    }
    return count(new LambdaQueryWrapper<Article>().eq(Article::getProjectId, projectId)) > 0;
  }

  /**
   * 构建文章查询条件
   *
   * @param queryDTO 查询参数DTO
   * @return LambdaQueryWrapper<Article> 查询条件构造器
   */
  private LambdaQueryWrapper<Article> queryWrapper(ArticleQueryDTO queryDTO) {
    LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
    boolean admin = true;
    try {
      admin = !sysUserService.isAdmin(StpUtil.getLoginIdAsLong());
    } catch (Exception ignored) {
    }

    // 根据ID列表查询
    qw.in(!ObjectUtils.isEmpty(queryDTO.getIds()), Article::getId, queryDTO.getIds());
    // 非管理员只能查看已发布的文章
    qw.eq(admin, Article::getStatus, 1);
    // 根据标题模糊查询
    qw.like(Objects.nonNull(queryDTO.getTitle()), Article::getTitle, queryDTO.getTitle());
    // 根据摘要模糊查询
    qw.like(Objects.nonNull(queryDTO.getSummary()), Article::getSummary, queryDTO.getSummary());
    // 排序
    qw.orderBy(
        Objects.nonNull(queryDTO.getOrderBy()),
        !Objects.equals(queryDTO.getOrderBy(), "desc"),
        Article::getCreateTime);
    return qw;
  }
}
