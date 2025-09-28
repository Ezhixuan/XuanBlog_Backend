package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.ArticleTag;
import com.ezhixuan.blog.domain.entity.Tag;
import com.ezhixuan.blog.mapper.ArticleTagMapper;
import com.ezhixuan.blog.service.ArticleTagService;
import com.ezhixuan.blog.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author ezhixuan
 * @description 针对表【article_tag(文章标签关联)】的数据库操作Service实现
 * @createDate 2025-09-13 10:40:23
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService {

  @Resource private TagService tagService;

  /**
   * 将 tag 与 article 建立连接
   *
   * @param articleId 文章 id
   * @param tagIds 标签 id 列表
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void link(Long articleId, List<Long> tagIds) {
    // 数据校验
    if (isNull(articleId) || isEmpty(tagIds)) {
      return;
    }
    // 检查数据是否存在,只绑定存在的数据
    List<Tag> tags = tagService.listByIds(tagIds);
    if (isEmpty(tags)) {
      return;
    }
    // 数据绑定
    // 1. 查询已绑定的标签数据
    Set<Long> linkedTagIds =
        new HashSet<>(
            listObjs(
                Wrappers.<ArticleTag>lambdaQuery()
                    .select(ArticleTag::getTagId)
                    .eq(ArticleTag::getArticleId, articleId)));
    Set<Long> livedTagIds = tags.stream().map(Tag::getId).collect(Collectors.toSet());
    tagIds =
        tagIds.stream()
            .filter(tagId -> !linkedTagIds.contains(tagId))
            .filter(livedTagIds::contains)
            .toList();
    if (isEmpty(tagIds)) {
      return;
    }
    List<ArticleTag> articleTagList =
        tagIds.stream()
            .map(
                tagId -> {
                  ArticleTag articleTag = new ArticleTag();
                  articleTag.setArticleId(articleId);
                  articleTag.setTagId(tagId);
                  return articleTag;
                })
            .toList();
    saveBatch(articleTagList);
  }

  /**
   * 删除文章标签关联
   *
   * @param articleId 文章 id
   */
  @Override
  public void unLinkByArticleId(Long articleId) {
    if (isNull(articleId)) {
      return;
    }
    removeById(articleId);
  }

  /**
   * 删除文章标签关联
   *
   * @param tagId 标签 id
   */
  @Override
  public void unLinkByTagId(Long tagId) {
    if (isNull(tagId)) {
      return;
    }
    remove(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getTagId, tagId));
  }

  /**
   * 获取文章标签关联
   *
   * @param articleIds 文章 id 列表
   * @return Map<文章 id, 标签列表>
   */
  @Override
  public Map<Long, List<Tag>> getLink(List<Long> articleIds) {
    if (isEmpty(articleIds)) {
      return Map.of();
    }

    // 查询文章标签关联信息
    List<ArticleTag> articleTagList =
        list(Wrappers.<ArticleTag>lambdaQuery().in(ArticleTag::getArticleId, articleIds));
    if (isEmpty(articleTagList)) {
      return Map.of();
    }

    // 查询所有相关的标签信息
    List<Long> tagIds = articleTagList.stream().map(ArticleTag::getTagId).distinct().toList();

    List<Tag> tags = tagService.listByIds(tagIds);
    if (isEmpty(tags)) {
      return Map.of();
    }

    // 创建标签ID到标签对象的映射
    Map<Long, Tag> tagMap = tags.stream().collect(Collectors.toMap(Tag::getId, tag -> tag));

    // 按文章ID分组文章标签关联信息
    Map<Long, List<ArticleTag>> articleTagMap =
        articleTagList.stream().collect(Collectors.groupingBy(ArticleTag::getArticleId));

    // 构建返回结果，使用完整的标签对象而不是新建的
    return articleTagMap.entrySet().stream()
        .map(
            entry -> {
              List<Tag> tagList =
                  entry.getValue().stream()
                      .map(articleTag -> tagMap.get(articleTag.getTagId()))
                      .filter(Objects::nonNull) // 确保标签存在
                      .toList();
              return Map.entry(entry.getKey(), tagList);
            })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * 获取标签关联的文章
   *
   * @param tagIds 标签 id 列表
   * @return List<文章 id>
   */
  @Override
  public List<Long> getLinkedArticleIds(List<Long> tagIds) {
    if (isEmpty(tagIds)) {
      return List.of();
    }
    return list(Wrappers.<ArticleTag>lambdaQuery().in(ArticleTag::getTagId, tagIds)).stream()
        .map(ArticleTag::getArticleId)
        .distinct()
        .toList();
  }

  /**
   * 获取标签使用数量
   *
   * @param num 数量
   * @return Map<标签 id, 使用数量>
   */
  @Override
  public Map<Long, Long> getTagUseCount(int num) {
    return list(Wrappers.<ArticleTag>lambdaQuery().last("GROUP BY tag_id")).stream()
        .collect(Collectors.groupingBy(ArticleTag::getTagId, Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(num)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
