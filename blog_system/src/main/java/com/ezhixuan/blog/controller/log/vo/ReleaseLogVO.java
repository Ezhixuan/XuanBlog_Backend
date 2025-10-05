package com.ezhixuan.blog.controller.log.vo;

import com.ezhixuan.blog.domain.entity.ReleaseLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 发布日志VO
 *
 * @author ezhixuan
 */
@Data
@Schema(description = "发布日志VO")
public class ReleaseLogVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "一句话标题")
    private String title;

    @Schema(description = "详情")
    private String content;

    @Schema(description = "类型：1-功能 2-修复 3-优化")
    private Integer type;

    @Schema(description = "级别：1-普通 2-重要 3-紧急")
    private Integer level;

    @Schema(description = "状态：0-草稿 1-发布")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 从实体对象转换
     *
     * @param releaseLog 实体对象
     * @return ReleaseLogVO
     */
    public static ReleaseLogVO fromEntity(ReleaseLog releaseLog) {
        if (releaseLog == null) {
            return null;
        }
        ReleaseLogVO vo = new ReleaseLogVO();
        vo.setId(releaseLog.getId());
        vo.setVersion(releaseLog.getVersion());
        vo.setTitle(releaseLog.getTitle());
        vo.setContent(releaseLog.getContent());
        vo.setType(releaseLog.getType());
        vo.setLevel(releaseLog.getLevel());
        vo.setStatus(releaseLog.getStatus());
        vo.setCreateTime(releaseLog.getCreateTime());
        return vo;
    }
}
