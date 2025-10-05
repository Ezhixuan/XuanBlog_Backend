package com.ezhixuan.blog.controller.log.dto;

import com.ezhixuan.blog.domain.entity.ReleaseLog;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 发布日志创建DTO
 *
 * @author ezhixuan
 */
@Data
@Schema(description = "发布日志创建DTO")
public class ReleaseLogCreateDTO {

    @Schema(description = "版本号")
    @NotNull(message = "版本号不能为空")
    @Size(max = 50, message = "版本号长度不能超过50个字符")
    private String version;

    @Schema(description = "一句话标题")
    @NotNull(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;

    @Schema(description = "详情")
    @NotNull(message = "详情不能为空")
    private String content;

    @Schema(description = "类型：1-功能 2-修复 3-优化")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @Schema(description = "级别：1-普通 2-重要 3-紧急")
    @NotNull(message = "级别不能为空")
    private Integer level;

    @Schema(description = "状态：0-草稿 1-发布")
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 转换为实体对象
     *
     * @return ReleaseLog 实体对象
     */
    public ReleaseLog toEntity() {
        ReleaseLog releaseLog = new ReleaseLog();
        releaseLog.setVersion(version);
        releaseLog.setTitle(title);
        releaseLog.setContent(content);
        releaseLog.setType(type);
        releaseLog.setLevel(level);
        releaseLog.setStatus(status);
        releaseLog.setCreateTime(LocalDateTime.now());
        return releaseLog;
    }
}
