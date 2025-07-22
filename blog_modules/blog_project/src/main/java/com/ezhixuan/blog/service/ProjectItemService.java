package com.ezhixuan.blog.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.dto.ProjectQueryDTO;
import com.ezhixuan.blog.domain.entity.ProjectItem;
import com.ezhixuan.blog.domain.vo.ProjectLinkArticleVo;

/**
* @author ezhixuan
* @description 针对表【project_item】的数据库操作Service
* @createDate 2025-07-05 22:13:11
*/
public interface ProjectItemService extends IService<ProjectItem> {

    /**
     * 查询项目列表
     * @author Ezhixuan
     * @param queryDTO 查询参数
     * @return IPage<ProjectItem> 项目列表
     */
    IPage<ProjectItem> queryListByDTO(ProjectQueryDTO queryDTO);

    /**
     * 获取用于关联文章的项目列表
     * @author Ezhixuan
     * @return List<ProjectLinkArticleVo>
     */
    List<ProjectLinkArticleVo> getLinkArticleList();
}
