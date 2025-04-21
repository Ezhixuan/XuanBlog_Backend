package com.ezhixuan.xuanblog_backend.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.xuanblog_backend.domain.constant.RedisKeyConstant;
import com.ezhixuan.xuanblog_backend.domain.entity.article.Article;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.ArticleService;
import com.ezhixuan.xuanblog_backend.service.ArticleThumbService;
import com.ezhixuan.xuanblog_backend.task.ThumbSyncTask;
import com.ezhixuan.xuanblog_backend.utils.RedisLuaScriptUtil;
import com.ezhixuan.xuanblog_backend.utils.RedisUtil;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleThumbServiceImpl implements ArticleThumbService {

    private final ArticleService articleService;
    private final RedisUtil redisUtil;

    /**
     * 获取博客的点赞数
     *
     * @param blogIds 博客 id 集合
     * @return 点赞集合 (id,点赞数)
     * @author Ezhixuan
     */
    @Override
    public Map<Long, Integer> get(Collection<Long> articleIds) {
        ThrowUtils.throwIf(CollectionUtils.isEmpty(articleIds), ErrorCode.PARAMS_ERROR);
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
        return setThumb(articleId);
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
}
