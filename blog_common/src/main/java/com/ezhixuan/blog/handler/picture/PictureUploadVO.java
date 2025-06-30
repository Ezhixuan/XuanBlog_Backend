package com.ezhixuan.blog.handler.picture;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PictureUploadVO {

    /**
     * id
     */
    private Long id;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 图片类型 1.博客内容图片 2.博客封面图片 3.博客用户头像
     */
    private Integer type;

}
