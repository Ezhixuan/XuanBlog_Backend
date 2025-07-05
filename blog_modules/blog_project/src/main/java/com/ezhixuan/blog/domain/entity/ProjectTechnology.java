package com.ezhixuan.blog.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProjectTechnology {
    @Schema(description="主键 id")
    @TableId(type = IdType.AUTO)
    private Long id;
    @Schema(description="技术栈描述")
    private String name;
}
