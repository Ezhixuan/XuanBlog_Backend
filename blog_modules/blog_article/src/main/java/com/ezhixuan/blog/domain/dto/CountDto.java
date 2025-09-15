package com.ezhixuan.blog.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用于进行数据统计
 *
 * @author Ezhixuan
 * @version 0.0.2beta
 */
@Data
public class CountDto {

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "数量")
    private Long count;
}
