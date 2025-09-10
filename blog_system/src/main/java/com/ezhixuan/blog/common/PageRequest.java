package com.ezhixuan.blog.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Objects;

@Data
public class PageRequest {

  public static final String ASC = "asc";
  public static final String DESC = "desc";
  public static final Integer PAGE_SIZE_NONE = -1;
  private static final Integer PAGE_NO = 1;
  private static final Integer PAGE_SIZE = 10;

  @Schema(description = "页码，从 1 开始")
  @NotNull(message = "页码不能为空")
  @Min(value = 1, message = "页码最小值为 1")
  private Integer pageNo = PAGE_NO;

  @Schema(description = "每页条数，最大值为 100")
  @NotNull(message = "每页条数不能为空")
  @Min(value = 1, message = "每页条数最小值为 1")
  @Max(value = 100, message = "每页条数最大值为 100")
  private Integer pageSize = PAGE_SIZE;

  @Schema(description = "排序方式")
  private String orderBy = ASC;

  public <T> IPage<T> toPage() {
    if (Objects.equals(pageSize, PAGE_SIZE_NONE)) {
      return new Page<>(pageNo, pageSize);
    }
    return new Page<>(pageNo, pageSize, true);
  }
}
