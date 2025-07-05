package com.ezhixuan.blog.service.impl;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.dto.ProjectQueryDTO;
import com.ezhixuan.blog.domain.entity.ProjectItem;
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
        lqw.in(!CollectionUtils.isEmpty(projectIds), ProjectItem::getId, projectIds);
        return lqw;
    }
}
