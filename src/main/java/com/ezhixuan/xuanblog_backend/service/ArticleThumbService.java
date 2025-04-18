package com.ezhixuan.xuanblog_backend.service;

public interface ArticleThumbService {

    /**
     * 获取博客的点赞数
     * @author Ezhixuan
     * @param blogId 博客 id
     * @return 点赞数
     */
    int get(Long blogId);

    /**
     * 对文章进行点赞
     * @author Ezhixuan
     * @param blogId 博客 Id
     * @return 点赞成功/取消点赞
     */
    boolean doThumb(Long blogId);

    /**
     * 判断当前用户是否已经进行过点赞
     * @author Ezhixuan
     * @param blogId 博客 Id
     * @return 已点赞/未点赞
     */
    boolean isThumb(Long blogId);
}
