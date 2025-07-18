package com.ezhixuan.blog.service.impl;

import static com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.entity.article.Article;
import com.ezhixuan.blog.domain.entity.article.ArticleThumb;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.exception.ThrowUtils;
import com.ezhixuan.blog.mapper.ArticleThumbMapper;
import com.ezhixuan.blog.service.ArticleService;
import com.ezhixuan.blog.service.ArticleThumbService;
import com.ezhixuan.blog.task.ThumbSyncTask;
import com.ezhixuan.blog.utils.RedisLuaScriptUtil;
import com.ezhixuan.blog.utils.RedisUtil;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleThumbServiceImpl extends ServiceImpl<ArticleThumbMapper, ArticleThumb>
        implements ArticleThumbService {

    private final ArticleService articleService;
    private final RedisUtil redisUtil;

    /**
     * 获取博客的点赞数
     *
     * @param articleIds 博客 id 集合
     * @return 点赞集合 (id,点赞数)
     * @author Ezhixuan
     */
    @Override
    public Map<Long, Integer> get(Collection<Long> articleIds) {
        ThrowUtils.throwIf(isEmpty(articleIds), ErrorCode.PARAMS_ERROR);
        return articleService.list(Wrappers.<Article>lambdaQuery().in(Article::getId, articleIds)).stream().collect(Collectors.toMap(Article::getId, Article::getLikeCount));
    }

    /**
     * 对文章进行点赞
     *
     * @param articleId 博客 Id
     * @return 点赞成功/取消点赞
     * @author Ezhixuan
     */
    @Override
    public boolean doThumb(Long articleId) {
        ThrowUtils.throwIf(Objects.isNull(articleId), ErrorCode.PARAMS_ERROR);
        ifNotExists(articleId);
        return setThumb(articleId);
    }

    private void ifNotExists(Long articleId) {
        if (redisUtil.hasKey(RedisKeyConstant.ARTICLE_THUMB_PRE_KEY + articleId)) {
            return;
        }
        syncToRedis(Collections.singletonList(articleId));
    }

    /**
     * 判断当前用户是否已经进行过点赞
     *
     * @param articleId 博客 Id
     * @return 已点赞/未点赞
     * @author Ezhixuan
     */
    @Override
    public boolean isThumb(Long articleId) {
        ThrowUtils.throwIf(Objects.isNull(articleId), ErrorCode.PARAMS_ERROR);
        return getThumb(articleId);
    }

    private boolean getThumb(Long articleId) {
        if (!StpUtil.isLogin()) {
            return false;
        }
        String userId = StpUtil.getLoginIdAsString();
        String key = getThumbKey() + articleId;

        Boolean thumb = redisUtil.hGet(key, userId);
        if (Objects.nonNull(thumb)) {
            thumb = !(boolean)thumb;
        } else {
            thumb = true;
        }
        return thumb;
    }

    /**
     * 获取该文章本日点赞数
     *
     * @param articleId 博客 id
     * @return 点赞数
     * @author Ezhixuan
     */
    @Override
    public int getThumbToday(Long articleId) {
        Object o = redisUtil.hGet(getThumbTempKey(), String.valueOf(articleId));
        if (Objects.isNull(o)) {
            return 0;
        }
        return Integer.parseInt(o.toString());
    }

    private boolean setThumb(Long articleId) {
        ThrowUtils.throwIf(!StpUtil.isLogin(), ErrorCode.NOT_LOGIN_ERROR, "如果喜欢本篇文章,请您登入后进行点赞");
        String loginUserId = StpUtil.getLoginIdAsString();
        return redisUtil.getRedisTemplate().execute(RedisLuaScriptUtil.THUMB_SCRIPT, Arrays.asList(getThumbKey() + articleId, getThumbTempKey()), loginUserId, articleId);
    }

    private String getThumbKey() {
        return RedisKeyConstant.ARTICLE_THUMB_PRE_KEY;
    }

    private String getThumbTempKey() {
        return RedisKeyConstant.ARTICLE_THUMB_TEMP_PRE_KEY + ThumbSyncTask.CURRENT_TIME;
    }

    /**
     * 将数据同步至 redis
     *
     * @param articleIds 博客 id 集合
     * @return void
     * @author Ezhixuan
     */
    @Override
    public void syncToRedis(Collection<Long> articleIds) {
        Map<Object, Object> articleUserIdMap = list(Wrappers.<ArticleThumb>lambdaQuery().in(ArticleThumb::getArticleId, articleIds)).stream().collect(Collectors.toMap(ArticleThumb::getArticleId, ArticleThumb::getUserId));
        if (isEmpty(articleUserIdMap)) {
            return;
        }
        articleIds.forEach(articleId -> {
            String thumbKey = RedisKeyConstant.ARTICLE_THUMB_PRE_KEY + articleId;
            Object userId = articleUserIdMap.get(articleId);
            redisUtil.hSet(thumbKey, userId.toString(), 1);
        });
    }
}
