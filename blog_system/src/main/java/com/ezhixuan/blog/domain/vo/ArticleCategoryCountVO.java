package com.ezhixuan.blog.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleCategoryCountVO extends ArticleCategoryVO{

    private int count;
}
