package com.ezhixuan.blog.controller.picture.dto;

import com.ezhixuan.blog.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片列表查询 Dto
 *
 * @author Ezhixuan
 * @version 0.0.1beta
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PictureQueryDTO extends PageRequest {

  @Schema(description = "图片类型")
  private Integer type;
}
