package com.ezhixuan.xuanblog_backend.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleTagCountVO extends ArticleTagVO{

    private int count;
}
