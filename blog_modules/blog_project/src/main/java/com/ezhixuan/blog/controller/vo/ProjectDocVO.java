package com.ezhixuan.blog.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "项目文档VO")
public class ProjectDocVO {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "文档标题")
    private String title;

    @Schema(description = "文档内容")
    private List<ProjectArticleDocVO> articles;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
