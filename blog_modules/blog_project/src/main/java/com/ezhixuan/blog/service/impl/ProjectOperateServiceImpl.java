package com.ezhixuan.blog.service.impl;

import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.domain.dto.ProjectCreateDTO;
import com.ezhixuan.blog.domain.dto.ProjectEditDTO;
import com.ezhixuan.blog.domain.dto.ProjectQueryDTO;
import com.ezhixuan.blog.domain.entity.ProjectItem;
import com.ezhixuan.blog.domain.vo.ProjectQueryVO;
import com.ezhixuan.blog.service.LinkProjectTechnologyService;
import com.ezhixuan.blog.service.ProjectItemService;
import com.ezhixuan.blog.service.ProjectOperateService;
import com.ezhixuan.blog.service.ProjectTechnologyService;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectOperateServiceImpl implements ProjectOperateService {

    private final ProjectItemService itemService;
    private final ProjectTechnologyService technologyService;
    private final LinkProjectTechnologyService linkService;

    /**
     * 查询项目列表
     *
     * @param queryDTO 查询参数
     * @return ProjectQueryVO 项目列表
     * @author Ezhixuan
     */
    @Override
    public IPage<ProjectQueryVO> getList(ProjectQueryDTO queryDTO) {
        queryDTO.setProjectIds(null);
        if (hasText(queryDTO.getTechnology())) {
            return queryByTechnology(queryDTO);
        }
        return queryByDTO(queryDTO);
    }

    private IPage<ProjectQueryVO> queryByDTO(ProjectQueryDTO queryDTO) {
        IPage<ProjectItem> page = itemService.queryListByDTO(queryDTO);
        return ProjectQueryDTO.convert(page, this::convert);
    }

    private ProjectQueryVO convert(ProjectItem item) {
        ProjectQueryVO vo = BeanUtil.copyProperties(item, ProjectQueryVO.class);
        List<Long> technologyIds = linkService.queryLink(vo.getId());
        List<String> technologyList = technologyService.getName(technologyIds);
        vo.setTechnologies(technologyList);
        fullViewAndStartData(item);
        return vo;
    }

    private void fullViewAndStartData(ProjectItem item) {
        if (!hasText(item.getLiveUrl())) {
            return;
        }
        /*
         todo Ezhixuan : kankan
         */
        return;
    }

    private IPage<ProjectQueryVO> queryByTechnology(ProjectQueryDTO queryDTO) {
        Long technologyId = technologyService.getIdByName(queryDTO.getTechnology());
        List<Long> projectIds = linkService.queryProjectId(technologyId);
        queryDTO.setProjectIds(projectIds);
        return queryByDTO(queryDTO);
    }

    /**
     * 创建项目
     *
     * @param createDTO 创建参数
     * @return ProjectQueryVO 项目信息
     * @author Ezhixuan
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectQueryVO save(ProjectCreateDTO createDTO) {
        List<Long> technologiesIds = technologyService.saveAll(createDTO.getTechnologies());
        ProjectItem projectItem = BeanUtil.copyProperties(createDTO, ProjectItem.class);
        projectItem.setCreateTime(LocalDateTime.now());
        itemService.save(projectItem);
        linkService.saveAll(projectItem.getId(), technologiesIds);
        return convert(projectItem);
    }

    /**
     * 编辑项目
     *
     * @param editDTO 编辑参数
     * @return ProjectQueryVO 项目信息
     */
    @Override
    public ProjectQueryVO edit(ProjectEditDTO editDTO) {
        List<Long> technologiesIds = technologyService.saveAll(editDTO.getTechnologies());
        ProjectItem projectItem = BeanUtil.copyProperties(editDTO, ProjectItem.class);
        itemService.updateById(projectItem);
        linkService.saveAll(projectItem.getId(), technologiesIds);
        return convert(projectItem);
    }

    /**
     * 推荐
     *
     * @param id 项目id
     * @author Ezhixuan
     */
    @Override
    public boolean featured(Long id) {
        ProjectItem item = itemService.getById(id);
        if (Objects.isNull(item)) {
            return false;
        }
        boolean res = !item.getFeatured();
        item.setFeatured(res);
        itemService.updateById(item);
        return res;
    }
}
