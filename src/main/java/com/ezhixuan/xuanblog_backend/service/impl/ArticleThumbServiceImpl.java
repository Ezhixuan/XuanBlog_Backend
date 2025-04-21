package com.ezhixuan.xuanblog_backend.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.xuanblog_backend.domain.constant.RedisKeyConstant;
import com.ezhixuan.xuanblog_backend.domain.entity.article.Article;
import com.ezhixuan.xuanblog_backend.exception.ErrorCode;
import com.ezhixuan.xuanblog_backend.exception.ThrowUtils;
import com.ezhixuan.xuanblog_backend.service.ArticleService;
import com.ezhixuan.xuanblog_backend.service.ArticleThumbService;
import com.ezhixuan.xuanblog_backend.utils.RedisUtil;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleThumbServiceImpl implements ArticleThumbService {

    private final ArticleService articleService;
    private final RedisUtil redisUtil;
    private final TransactionTemplate transactionTemplate;

    /**
     * 获取博客的点赞数
     *
     * @param blogIds 博客 id 集合
     * @return 点赞集合 (id,点赞数)
     * @author Ezhixuan
     */
    @Override
    public Map<Long, Integer> get(Collection<Long> blogIds) {
        ThrowUtils.throwIf(CollectionUtils.isEmpty(blogIds), ErrorCode.PARAMS_ERROR);
        return articleService.list(Wrappers.<Article>lambdaQuery().in(Article::getId, blogIds)).stream().collect(Collectors.toMap(Article::getId, Article::getLikeCount));
    }

    /**
     * 对文章进行点赞
     *
     * @param blogId 博客 Id
     * @return 点赞成功/取消点赞
     * @author Ezhixuan
     */
    @Override
    public boolean doThumb(Long blogId) {
        ThrowUtils.throwIf(Objects.isNull(blogId), ErrorCode.PARAMS_ERROR);

        boolean thumb = getThumb(blogId);
        setThumb(blogId, thumb);

        return thumb;
    }

    /**
     * 判断当前用户是否已经进行过点赞
     *
     * @param blogId 博客 Id
     * @return 已点赞/未点赞
     * @author Ezhixuan
     */
    @Override
    public boolean isThumb(Long blogId) {
        ThrowUtils.throwIf(Objects.isNull(blogId), ErrorCode.PARAMS_ERROR);
        return getThumb(blogId);
    }

    private boolean getThumb(Long blogId) {
        if (!StpUtil.isLogin()) {
            return false;
        }
        String userId = StpUtil.getLoginIdAsString();
        String key = getThumbKey(blogId);

        Boolean thumb = redisUtil.hGet(key, userId);
        if (Objects.nonNull(thumb)) {
            thumb = !(boolean)thumb;
        } else {
            thumb = true;
        }
        return thumb;
    }

    private void setThumb(Long blogId, boolean thumb) {
        ThrowUtils.throwIf(!StpUtil.isLogin(), ErrorCode.NOT_LOGIN_ERROR, "如果喜欢本篇文章,请您登入后进行点赞");
        String loginUserId = StpUtil.getLoginIdAsString();
        String sql = thumb ? "like_count = like_count + 1 " : "like_count = like_count - 1 ";

        synchronized (loginUserId.intern()) {
            transactionTemplate.execute(status -> {
                boolean update = articleService.update(Wrappers.<Article>lambdaUpdate().eq(Article::getId, blogId).setSql(sql));
                boolean success = update && redisUtil.hSet(getThumbKey(blogId), loginUserId, thumb);
                ThrowUtils.throwIf(!success, ErrorCode.OPERATION_ERROR);
                return null;
            });
        }
    }

    private String getThumbKey(Long blogId) {
        return RedisKeyConstant.ARTICLE_THUMB_PRE_KEY + blogId + ":";
    }
}
