package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.controller.dto.ProjectQueryDTO;
import com.ezhixuan.blog.domain.entity.Project;
import com.ezhixuan.blog.controller.vo.ProjectLinkArticleVo;
import com.ezhixuan.blog.mapper.ProjectMapper;
import com.ezhixuan.blog.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author ezhixuan
 * @description 针对表【project_item】的数据库操作Service实现
 * @createDate 2025-07-05 22:13:11
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    /**
     * 查询项目列表
     *
     * @param queryDTO 查询参数
     * @return IPage<ProjectItem> 项目列表
     * @author Ezhixuan
     */
    @Override
    public IPage<Project> queryListByDTO(ProjectQueryDTO queryDTO) {
        LambdaQueryWrapper<Project> lqw = queryWrapper(queryDTO);
        return page(queryDTO.toPage(), lqw);
    }

    private LambdaQueryWrapper<Project> queryWrapper(ProjectQueryDTO queryDTO) {
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<>();
        if (Objects.isNull(queryDTO)) {
            return lqw;
        }
        Boolean featured = queryDTO.getFeatured();
        List<Long> projectIds = queryDTO.getProjectIds();

        lqw.eq(nonNull(featured), Project::getFeatured, featured);
        lqw.in(!isEmpty(projectIds), Project::getId, projectIds);
        return lqw;
    }

    /**
     * 获取用于关联文章的项目列表
     *
     * @return List<ProjectLinkArticleVo>
     * @author Ezhixuan
     */
    @Override
    public List<ProjectLinkArticleVo> getLinkArticleList() {
        List<Project> projectList = list();
        if (isEmpty(projectList)) {
            return Collections.emptyList();
        }
        return projectList.stream().map(item -> {
            ProjectLinkArticleVo projectLinkArticleVo = new ProjectLinkArticleVo();
            projectLinkArticleVo.setId(item.getId());
            projectLinkArticleVo.setTitle(item.getTitle());
            return projectLinkArticleVo;
        }).toList();
    }
}
