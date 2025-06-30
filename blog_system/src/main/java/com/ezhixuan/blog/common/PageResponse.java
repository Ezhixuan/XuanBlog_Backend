package com.ezhixuan.blog.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    private List<T> data;

    private long total;
}
