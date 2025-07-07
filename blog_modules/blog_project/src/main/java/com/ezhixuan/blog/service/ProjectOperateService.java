package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.domain.dto.ProjectCreateDTO;
import com.ezhixuan.blog.domain.dto.ProjectEditDTO;
import com.ezhixuan.blog.domain.dto.ProjectQueryDTO;
import com.ezhixuan.blog.domain.vo.ProjectQueryVO;

public interface ProjectOperateService {

    /**
     * 查询项目列表
     *
     * @author Ezhixuan
     * @param queryDTO 查询参数
     * @return ProjectQueryVO 项目列表
     */
    IPage<ProjectQueryVO> getList(ProjectQueryDTO queryDTO);

    /**
     * 创建项目
     * @author Ezhixuan
     * @param createDTO 创建参数
     * @return ProjectQueryVO 项目信息
     */
    ProjectQueryVO save(ProjectCreateDTO createDTO);

    /**
     * 编辑项目
     * @param editDTO 编辑参数
     * @return ProjectQueryVO 项目信息
     */
    ProjectQueryVO edit(ProjectEditDTO editDTO);

    /**
     * 推荐
     * @author Ezhixuan
     * @param id 项目id
     */
    boolean featured(Long id);
}
