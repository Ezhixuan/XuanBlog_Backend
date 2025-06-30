package com.ezhixuan.blog.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;

@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";

    public <T> IPage<T> toIPage() {
        return new Page<>(this.current, this.getPageSize());
    }

    public static <A, B> IPage<B> convert(IPage<A> sourcePage, Converter<A, B> converter) {
        // 创建新的 IPage 对象，保持分页信息一致
        IPage<B> targetPage = new Page<>(sourcePage.getCurrent(), sourcePage.getSize(), sourcePage.getTotal());
        // 转换数据内容
        if (!ObjectUtils.isEmpty(sourcePage.getRecords())) {
            List<B> targetRecords = sourcePage.getRecords().stream()
                    .map(converter::convert)
                    .collect(Collectors.toList());
            // 设置新的数据内容
            targetPage.setRecords(targetRecords);
        }
        return targetPage;
    }

    public static <A,B> IPage<B> convert(IPage<A> sourcePage, List<B> record) {
        IPage<B> targetPage = new Page<>(sourcePage.getCurrent(), sourcePage.getSize(), sourcePage.getTotal());
        targetPage.setRecords(record);
        return targetPage;
    }
}
