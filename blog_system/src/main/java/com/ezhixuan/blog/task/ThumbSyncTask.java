package com.ezhixuan.blog.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ezhixuan.blog.annotation.Log;
import com.ezhixuan.blog.domain.constant.RedisKeyConstant;
import com.ezhixuan.blog.domain.entity.article.Article;
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
     * @author Ezhixuan
     */
    @Log
    @Scheduled(fixedRate = 1000L * 60 * 60)
    @Transactional(rollbackFor = Exception.class)
    public void syncThumb2DB() {
        String tempKey = RedisKeyConstant.ARTICLE_THUMB_TEMP_PRE_KEY + CURRENT_TIME;
        Map<Object, Object> entries = redisUtil.getRedisTemplate().opsForHash().entries(tempKey);
        if (CollectionUtils.isEmpty(entries)) return;

        List<Article> articleList = articleService.list(Wrappers.<Article>lambdaQuery().in(Article::getId, entries.keySet()));

        articleList.forEach(article -> {
            article.setLikeCount(article.getLikeCount() + Integer.parseInt(entries.get(article.getId().toString()).toString()));
        });

        articleService.updateBatchById(articleList);

        Thread.startVirtualThread(() -> {
            redisUtil.cleanCaches(tempKey);
        });
    }

    public void syncThumbUser2DB() {

    }

    @PostConstruct
    public void syncThumbUser2Redis() {
        
    }

}
