package com.ezhixuan.blog.common;

import com.ezhixuan.blog.entity.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageResponse<T> extends BaseResponse<PageResVo<T>> implements Serializable {

  @Serial private static final long serialVersionUID = -8234567890123456789L;

  public PageResponse(PageResVo<T> data) {
    super(data);
  }

  public PageResponse() {
    super();
  }
}
