package com.ezhixuan.blog.service.impl;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.dto.ProjectQueryDTO;
import com.ezhixuan.blog.domain.entity.ProjectItem;
import com.ezhixuan.blog.domain.vo.ProjectLinkArticleVo;
import com.ezhixuan.blog.mapper.ProjectItemMapper;
import com.ezhixuan.blog.service.ProjectItemService;

/**
 * @author ezhixuan
 * @description 针对表【project_item】的数据库操作Service实现
 * @createDate 2025-07-05 22:13:11
 */
@Service
public class ProjectItemServiceImpl extends ServiceImpl<ProjectItemMapper, ProjectItem> implements ProjectItemService {

    /**
     * 查询项目列表
     *
     * @param queryDTO 查询参数
     * @return IPage<ProjectItem> 项目列表
     * @author Ezhixuan
     */
    @Override
    public IPage<ProjectItem> queryListByDTO(ProjectQueryDTO queryDTO) {
        LambdaQueryWrapper<ProjectItem> lqw = queryWrapper(queryDTO);
        return page(queryDTO.toIPage(), lqw);
    }

    private LambdaQueryWrapper<ProjectItem> queryWrapper(ProjectQueryDTO queryDTO) {
        LambdaQueryWrapper<ProjectItem> lqw = new LambdaQueryWrapper<>();
        if (Objects.isNull(queryDTO)) {
            return lqw;
        }
        Boolean featured = queryDTO.getFeatured();
        List<Long> projectIds = queryDTO.getProjectIds();

        lqw.eq(nonNull(featured), ProjectItem::getFeatured, featured);
        lqw.in(!isEmpty(projectIds), ProjectItem::getId, projectIds);
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
        List<ProjectItem> projectItemList = list();
        if (isEmpty(projectItemList)) {
            return Collections.emptyList();
        }
        return projectItemList.stream().map(item -> {
            ProjectLinkArticleVo projectLinkArticleVo = new ProjectLinkArticleVo();
            projectLinkArticleVo.setId(item.getId());
            projectLinkArticleVo.setTitle(item.getTitle());
            return projectLinkArticleVo;
        }).toList();
    }
}
