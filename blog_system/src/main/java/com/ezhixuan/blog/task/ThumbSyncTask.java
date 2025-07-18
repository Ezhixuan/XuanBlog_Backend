package com.ezhixuan.blog.task;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.entity.article.Article;
import com.ezhixuan.blog.domain.entity.article.ArticleThumb;
import com.ezhixuan.blog.service.ArticleService;
import com.ezhixuan.blog.service.ArticleThumbService;
import com.ezhixuan.blog.utils.RedisUtil;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ThumbSyncTask {

    public static String CURRENT_TIME = "currentTime";
    private final RedisUtil redisUtil;
    private final ArticleService articleService;
    private final ArticleThumbService thumbService;

    private static final String ALL_THUMB_KEY = RedisKeyConstant.ARTICLE_THUMB_PRE_KEY + "*";

    @PostConstruct
    public void init() {
        updateCurrentTimeTask();
    }

    /**
     * 定时更新时间以便任务统计
     *
     * @author Ezhixuan
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateCurrentTimeTask() {
        LocalDate localDate = LocalDate.now();
        CURRENT_TIME = localDate.format(DateTimeFormatter.ofPattern("MM:dd"));
    }

    /**
     * 每小时统计一次 Thumb
     *
     * @author Ezhixuan
     */
    @Log
    @Scheduled(fixedRate = 1000L * 60 * 60)
    @Transactional(rollbackFor = Exception.class)
    public void syncThumb2DB() {
        String tempKey = RedisKeyConstant.ARTICLE_THUMB_TEMP_PRE_KEY + CURRENT_TIME;
        Map<Object, Object> entries = redisUtil.getRedisTemplate().opsForHash().entries(tempKey);
        if (isEmpty(entries))
            return;

        List<Article> articleList =
            articleService.list(Wrappers.<Article>lambdaQuery().in(Article::getId, entries.keySet()));

        articleList.forEach(article -> {
            article.setLikeCount(
                article.getLikeCount() + Integer.parseInt(entries.get(article.getId().toString()).toString()));
        });

        articleService.updateBatchById(articleList);
        redisUtil.cleanCaches(tempKey);
    }

    @Log
    @Scheduled(fixedRate = 1000L * 60 * 60)
    @Transactional(rollbackFor = Exception.class)
    public void syncThumbUser2DB() {
        List<String> keys = redisUtil.scan(ALL_THUMB_KEY);
        if (isEmpty(keys)) {
            return;
        }

        List<Long> articleIds = keys.stream()
                .map(key -> Long.valueOf(key.replace(RedisKeyConstant.ARTICLE_THUMB_PRE_KEY, "")))
                .toList();

        if (isEmpty(articleIds)) {
            return;
        }

        Map<Object, List<ArticleThumb>> dbThumbsMap =
                thumbService.list(Wrappers.<ArticleThumb>lambdaQuery().in(ArticleThumb::getArticleId, articleIds)).stream()
                        .collect(Collectors.groupingBy(ArticleThumb::getArticleId));

        List<ArticleThumb> waitAddList = new ArrayList<>();
        List<ArticleThumb> waitRemoveList = new ArrayList<>();

        keys.forEach(key -> {
            Long articleId = Long.valueOf(key.replace(RedisKeyConstant.ARTICLE_THUMB_PRE_KEY, ""));
            Map<Object, Object> redisEntries = redisUtil.getRedisTemplate().opsForHash().entries(key);

            Set<Long> redisLikedUserIds = redisEntries.entrySet().stream()
                    .filter(entry -> Integer.valueOf(1).equals(entry.getValue()))
                    .map(entry -> Long.valueOf(entry.getKey().toString()))
                    .collect(Collectors.toSet());

            List<ArticleThumb> dbArticleThumbs = dbThumbsMap.getOrDefault(articleId, Collections.emptyList());
            Map<Object, ArticleThumb> dbUserIdMap = dbArticleThumbs.stream()
                    .collect(Collectors.toMap(ArticleThumb::getUserId, Function.identity()));
            Set<Object> dbUserIds = dbUserIdMap.keySet();

            redisLikedUserIds.stream()
                    .filter(userId -> !dbUserIds.contains(userId))
                    .forEach(userId -> {
                        ArticleThumb newThumb = new ArticleThumb();
                        newThumb.setArticleId(articleId);
                        newThumb.setUserId(userId);
                        waitAddList.add(newThumb);
                    });

            dbUserIds.stream()
                    .filter(userId -> !redisLikedUserIds.contains(userId))
                    .forEach(userId -> waitRemoveList.add(dbUserIdMap.get(userId)));
        });

        if (!isEmpty(waitAddList)) {
            thumbService.saveBatch(waitAddList);
        }
        if (!isEmpty(waitRemoveList)) {
            thumbService.removeBatchByIds(waitRemoveList);
        }
    }

    @Log
    @PostConstruct
    @Scheduled(fixedRate = 1000L * 60 * 60 * 24 * 7)
    public void syncThumbUser2Redis() {
        redisUtil.cleanCaches(ALL_THUMB_KEY);
        List<Long> articleIds =
            articleService.list(Wrappers.<Article>lambdaQuery().orderByDesc(Article::getLikeCount).last("limit 20"))
                .stream().map(Article::getId).toList();
        if (isEmpty(articleIds)) {
            return;
        }
        thumbService.syncToRedis(articleIds);
    }

}
