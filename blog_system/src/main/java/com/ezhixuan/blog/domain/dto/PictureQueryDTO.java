package com.ezhixuan.blog.domain.dto;

import com.ezhixuan.blog.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图片列表查询 Dto
 *
 * @author Ezhixuan
 * @version 0.0.1beta
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PictureQueryDTO extends PageRequest implements Serializable {

  @Serial private static final long serialVersionUID = 7854060083783577651L;

  @Schema(description = "图片类型")
  private int type;
}
