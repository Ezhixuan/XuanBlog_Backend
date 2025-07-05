package com.ezhixuan.blog.service;

import com.ezhixuan.blog.domain.entity.LinkProjectTechnology;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ezhixuan
* @description 针对表【link_project_technology】的数据库操作Service
* @createDate 2025-07-06 01:36:07
*/
public interface LinkProjectTechnologyService extends IService<LinkProjectTechnology> {

    /**
     * 获取技术栈 id
     * @author Ezhixuan
     * @param projectId 项目 id
     * @return List<Long> 技术栈 id
     */
    List<Long> queryLink(Long projectId);

    /**
     * 获取项目 id
     * @author Ezhixuan
     * @param technologyId 技术栈 id
     * @return List<Long> 项目 id
     */
    List<Long> queryProjectId(Long technologyId);

    /**
     * 建立连接
     * @author Ezhixuan
     * @param projectId 项目 id
     * @param technologiesIds 技术栈 id
     */
    void saveAll(Long projectId, List<Long> technologiesIds);
}
