package com.ezhixuan.blog.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LinkProjectTechnology {

    @Schema(description="主键 id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description="项目 id")
    @TableField("project_id")
    private Long projectId;

    @Schema(description="技术栈 id")
    @TableField("technology_id")
    private Long technologyId;
}
