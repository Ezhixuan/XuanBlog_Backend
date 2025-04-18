package com.ezhixuan.xuanblog_backend.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

    private final static String THUMB = "blog:thumb:";

    /**
     * 获取博客的点赞数
     *
     * @param blogId 博客 id
     * @return 点赞数
     * @author Ezhixuan
     */
    @Override
    public int get(Long blogId) {
        ThrowUtils.throwIf(Objects.isNull(blogId), ErrorCode.PARAMS_ERROR);
        return articleService.getObj(
            Wrappers.<Article>lambdaQuery().select(Article::getLikeCount).eq(Article::getId, blogId), o -> (int)o);
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
        long userId = StpUtil.getLoginIdAsLong();
        String key = getThumbKey(blogId, userId);

        Object thumbObj = redisUtil.get(key);
        if (Objects.nonNull(thumbObj)) {
            thumbObj = !(boolean)thumbObj;
        } else {
            thumbObj = true;
        }
        return (boolean)thumbObj;
    }

    private void setThumb(Long blogId, boolean thumb) {
        ThrowUtils.throwIf(!StpUtil.isLogin(), ErrorCode.NOT_LOGIN_ERROR, "如果喜欢本篇文章,请您登入后进行点赞");
        String loginUserId = StpUtil.getLoginIdAsString();
        String sql = thumb ? "like_count = like_count + 1 " : "like_count = like_count - 1 ";

        synchronized (loginUserId.intern()) {
            transactionTemplate.execute(status -> {
                boolean update = articleService.update(Wrappers.<Article>lambdaUpdate().eq(Article::getId, blogId).setSql(sql));
                boolean success = update && redisUtil.set(getThumbKey(blogId, loginUserId), thumb);
                ThrowUtils.throwIf(!success, ErrorCode.OPERATION_ERROR);
                return null;
            });
        }
    }

    private String getThumbKey(Long blogId, Object id) {
        return THUMB + blogId + ":" + id;
    }
}
