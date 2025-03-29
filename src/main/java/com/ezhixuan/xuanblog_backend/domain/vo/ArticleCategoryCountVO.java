package com.ezhixuan.xuanblog_backend.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleCategoryCountVO extends ArticleCategoryVO{

    private int count;
}
