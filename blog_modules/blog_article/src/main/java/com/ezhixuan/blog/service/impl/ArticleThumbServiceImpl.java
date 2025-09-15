package com.ezhixuan.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.entity.ArticleThumb;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.mapper.ArticleThumbMapper;
import com.ezhixuan.blog.service.ArticleThumbService;
import com.ezhixuan.blog.task.ThumbSyncTask;
import com.ezhixuan.blog.utils.RedisLuaScriptUtil;
import com.ezhixuan.blog.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty;
import static java.util.Objects.isNull;

/**
 * 文章点赞服务实现类
 *
 * @version 0.0.2beta
 * @author Ezhixuan
 */
@Service
@RequiredArgsConstructor
public class ArticleThumbServiceImpl extends ServiceImpl<ArticleThumbMapper, ArticleThumb>
    implements ArticleThumbService {

  private final RedisUtil redisUtil;

  /**
   * 对文章进行点赞操作
   *
   * @param articleId 文章ID
   * @return boolean 点赞是否成功
   */
  @Override
  public boolean doThumb(Long articleId) {
    if (isNull(articleId)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章ID不能为空");
    }
    ifNotExists(articleId);
    return setThumb(articleId);
  }

  /**
   * 获取文章当天的点赞数
   *
   * @param articleId 文章ID
   * @return int 当天点赞数
   */
  @Override
  public int getThumbToday(Long articleId) {
    Object o = redisUtil.hGet(getThumbTempKey(), String.valueOf(articleId));
    if (isNull(o)) {
      return 0;
    }
    return Integer.parseInt(o.toString());
  }

  /**
   * 将文章点赞数据同步到Redis
   *
   * @param articleIds 文章ID集合
   */
  @Override
  public void syncToRedis(Collection<Long> articleIds) {
    Map<Object, Object> articleUserIdMap =
        list(Wrappers.<ArticleThumb>lambdaQuery().in(ArticleThumb::getArticleId, articleIds))
            .stream()
            .collect(Collectors.toMap(ArticleThumb::getArticleId, ArticleThumb::getUserId));
    if (isEmpty(articleUserIdMap)) {
      return;
    }
    articleIds.forEach(
        articleId -> {
          String thumbKey = RedisKeyConstant.ARTICLE_THUMB_PRE_KEY + articleId;
          Object userId = articleUserIdMap.get(articleId);
          redisUtil.hSet(thumbKey, userId.toString(), 1);
        });
  }

  /**
   * 检查文章点赞数据是否存在，如果不存在则同步到Redis
   *
   * @param articleId 文章ID
   */
  private void ifNotExists(Long articleId) {
    if (redisUtil.hasKey(RedisKeyConstant.ARTICLE_THUMB_PRE_KEY + articleId)) {
      return;
    }
    syncToRedis(Collections.singletonList(articleId));
  }

  /**
   * 设置文章点赞
   *
   * @param articleId 文章ID
   * @return boolean 点赞是否成功
   */
  private boolean setThumb(Long articleId) {
    ThrowUtils.throwIf(!StpUtil.isLogin(), ErrorCode.NOT_LOGIN_ERROR, "如果喜欢本篇文章,请您登入后进行点赞");
    String loginUserId = StpUtil.getLoginIdAsString();
    return redisUtil
        .getRedisTemplate()
        .execute(
            RedisLuaScriptUtil.THUMB_SCRIPT,
            Arrays.asList(getThumbKey() + articleId, getThumbTempKey()),
            loginUserId,
            articleId);
  }

  /**
   * 获取文章点赞的Redis Key前缀
   *
   * @return String Redis Key前缀
   */
  private String getThumbKey() {
    return RedisKeyConstant.ARTICLE_THUMB_PRE_KEY;
  }

  /**
   * 获取临时点赞数据的Redis Key
   *
   * @return String 临时点赞数据的Redis Key
   */
  private String getThumbTempKey() {
    return RedisKeyConstant.ARTICLE_THUMB_TEMP_PRE_KEY + ThumbSyncTask.CURRENT_TIME;
  }
}
