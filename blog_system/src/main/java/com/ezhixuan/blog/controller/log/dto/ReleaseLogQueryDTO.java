package com.ezhixuan.blog.controller.log.dto;

import com.ezhixuan.blog.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发布日志查询DTO
 *
 * @author ezhixuan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "发布日志查询DTO")
public class ReleaseLogQueryDTO extends PageRequest {

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "标题关键词")
    private String title;

    @Schema(description = "类型：1-功能 2-修复 3-优化")
    private Integer type;

    @Schema(description = "级别：1-普通 2-重要 3-紧急")
    private Integer level;

    @Schema(description = "状态：0-草稿 1-发布")
    private Integer status;
}
