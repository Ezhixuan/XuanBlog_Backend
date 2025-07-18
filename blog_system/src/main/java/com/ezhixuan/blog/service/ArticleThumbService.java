package com.ezhixuan.blog.service;

import java.util.Collection;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.article.ArticleThumb;

public interface ArticleThumbService extends IService<ArticleThumb> {

    /**
     * 获取博客的点赞数
     * @param articleIds 博客 id 集合
     * @return 点赞集合 (id,点赞数)
     * @author Ezhixuan
     */
    Map<Long, Integer> get(Collection<Long> articleIds);

    /**
     * 对文章进行点赞
     * @author Ezhixuan
     * @param articleId 博客 Id
     * @return 点赞成功/取消点赞
     */
    boolean doThumb(Long articleId);

    /**
     * 判断当前用户是否已经进行过点赞
     * @author Ezhixuan
     * @param articleId 博客 Id
     * @return 已点赞/未点赞
     */
    boolean isThumb(Long articleId);

    /**
     * 获取该文章本日点赞数
     * @author Ezhixuan
     * @param articleId 博客 id
     * @return 点赞数
     */
    int getThumbToday(Long articleId);

    /**
     * 将数据同步至 redis
     * @author Ezhixuan
     * @param articleIds 博客 id 集合
     * @return void
     */
    void syncToRedis(Collection<Long> articleIds);
}
