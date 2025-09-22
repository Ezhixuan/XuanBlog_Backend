package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.controller.dto.ProjectDocCreateDTO;
import com.ezhixuan.blog.controller.dto.ProjectDocQueryDTO;
import com.ezhixuan.blog.controller.dto.ProjectDocUpdateDTO;
import com.ezhixuan.blog.domain.entity.ProjectDoc;

/**
* @author ezhixuan
* @description 针对表【project_doc(项目文档)】的数据库操作Service
* @createDate 2025-09-18 14:39:11
*/
public interface ProjectDocService extends IService<ProjectDoc> {

    /**
     * 创建项目文档
     * @param createDTO 创建参数
     * @return 项目文档
     */
    ProjectDoc create(ProjectDocCreateDTO createDTO);

    /**
     * 更新项目文档
     * @param updateDTO 更新参数
     * @return 项目文档
     */
    ProjectDoc update(ProjectDocUpdateDTO updateDTO);

    /**
     * 根据ID删除项目文档
     * @param id 项目文档ID
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 分页查询项目文档
     * @param queryDTO 查询参数
     * @return 项目文档分页结果
     */
    IPage<ProjectDoc> pageByDTO(ProjectDocQueryDTO queryDTO);
}
