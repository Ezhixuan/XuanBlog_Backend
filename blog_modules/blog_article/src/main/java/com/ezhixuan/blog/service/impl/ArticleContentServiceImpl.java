package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.ArticleContent;
import com.ezhixuan.blog.domain.entity.SysPicture;
import com.ezhixuan.blog.domain.enums.PictureTypeEnum;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.mapper.ArticleContentMapper;
import com.ezhixuan.blog.service.ArticleContentService;
import com.ezhixuan.blog.service.PictureUsageService;
import com.ezhixuan.blog.service.SysPictureService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author ezhixuan
 * @description 针对表【article_content(文章内容（延迟加载）)】的数据库操作Service实现
 * @createDate 2025-09-13 10:40:23
 */
@Slf4j
@Service
public class ArticleContentServiceImpl extends ServiceImpl<ArticleContentMapper, ArticleContent>
    implements ArticleContentService {

  @Resource SysPictureService pictureService;
  @Resource PictureUsageService pictureUsageService;

  /**
   * 将 content 与 article 建立连接
   *
   * @param articleId 文章 id
   * @param content 内容
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void link(Long articleId, String content) {
    if (isNull(articleId) || isNull(content)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章 id 内容不能为空");
    }
    ArticleContent articleContent = new ArticleContent();
    articleContent.setArticleId(articleId);
    articleContent.setContent(content);
    List<String> pictureUrlList = parsePictureUrl(content);
    if (!isEmpty(pictureUrlList)) {
      List<SysPicture> pictureList =
          pictureService.list(
              Wrappers.<SysPicture>lambdaQuery().in(SysPicture::getUrl, pictureUrlList));
      List<Long> pictureIds = pictureList.stream().map(SysPicture::getId).toList();
      pictureUsageService.link(PictureTypeEnum.ARTICLE_CONTENT, articleId, pictureIds);
      articleContent.setPictureIds(pictureIds);
    }
    articleContent.setUpdateTime(LocalDateTime.now());
    saveOrUpdate(articleContent);
  }

  /**
   * 删除文章内容
   *
   * @param articleId 文章 id
   */
  @Override
  public void unLink(Long articleId) {
    if (isNull(articleId)) {
      return;
    }
    ArticleContent articleContent = getById(articleId);
    if (isNull(articleContent)) {
      return;
    }
    List<Long> pictureIds = articleContent.getPictureIdList();
    removeById(articleId);
    if (isEmpty(pictureIds)) {
      return;
    }
    pictureUsageService.unLink(PictureTypeEnum.ARTICLE_CONTENT, articleId, pictureIds);
  }

  /**
   * 根据文章 id 获取文章内容
   *
   * @param articleId 文章 id
   * @return 文章内容
   */
  @Override
  public String getContentByArticleId(Long articleId) {
    if (isNull(articleId)) {
      log.error("文章 id 不能为空");
      return "";
    }
    ArticleContent articleContent =
        getOne(Wrappers.<ArticleContent>lambdaQuery().eq(ArticleContent::getArticleId, articleId));
    if (isNull(articleContent)) {
      return "";
    }
    return articleContent.getContent();
  }

  /**
   * 解析 Markdown 内容中的图片 URL
   *
   * @param content Markdown 内容
   * @return 图片 URL 列表
   */
  private List<String> parsePictureUrl(String content) {
    List<String> pictureUrls = new ArrayList<>();
    if (content == null || content.isEmpty()) {
      return pictureUrls;
    }

    // Markdown 图片语法: ![alt text](url)
    // 正则表达式匹配图片链接
    Pattern pattern = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\)");
    Matcher matcher = pattern.matcher(content);

    while (matcher.find()) {
      String url = matcher.group(2);
      if (url != null && !url.isEmpty()) {
        pictureUrls.add(url);
      }
    }

    return pictureUrls;
  }
}
