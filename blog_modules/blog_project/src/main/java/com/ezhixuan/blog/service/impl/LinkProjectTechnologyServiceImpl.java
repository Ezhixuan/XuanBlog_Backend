package com.ezhixuan.blog.service.impl;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.LinkProjectTechnology;
import com.ezhixuan.blog.mapper.LinkProjectTechnologyMapper;
import com.ezhixuan.blog.service.LinkProjectTechnologyService;

/**
 * @author ezhixuan
 * @description 针对表【link_project_technology】的数据库操作Service实现
 * @createDate 2025-07-06 01:36:07
 */
@Service
public class LinkProjectTechnologyServiceImpl extends ServiceImpl<LinkProjectTechnologyMapper, LinkProjectTechnology>
    implements LinkProjectTechnologyService {

    /**
     * 获取技术栈 id
     *
     * @param projectId 项目 id
     * @return List<Long> 技术栈 id
     * @author Ezhixuan
     */
    @Override
    public List<Long> queryLink(Long projectId) {
        if (isNull(projectId)) {
            return Collections.emptyList();
        }
        return listObjs(Wrappers.<LinkProjectTechnology>lambdaQuery().select(LinkProjectTechnology::getTechnologyId)
            .eq(LinkProjectTechnology::getProjectId, projectId));
    }

    /**
     * 获取项目 id
     *
     * @param technologyId 技术栈 id
     * @return id 项目 id
     * @author Ezhixuan
     */
    @Override
    public List<Long> queryProjectId(Long technologyId) {
        if (isNull(technologyId)) {
            return Collections.emptyList();
        }
        return listObjs(Wrappers.<LinkProjectTechnology>lambdaQuery().select(LinkProjectTechnology::getProjectId)
            .eq(LinkProjectTechnology::getTechnologyId, technologyId));
    }

    /**
     * 建立连接
     *
     * @param projectId       项目 id
     * @param technologiesIds 技术栈 id
     * @author Ezhixuan
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(Long projectId, List<Long> technologiesIds) {
        List<LinkProjectTechnology> list = list(Wrappers.<LinkProjectTechnology>lambdaQuery().eq(LinkProjectTechnology::getProjectId, projectId));
        if (!CollectionUtils.isEmpty(list)) {
            removeBatchByIds(list);
        }
        List<LinkProjectTechnology> projectTechnologyList = technologiesIds.stream().map(technologyId -> {
            LinkProjectTechnology link = new LinkProjectTechnology();
            link.setProjectId(projectId);
            link.setTechnologyId(technologyId);
            return link;
        }).toList();
        saveBatch(projectTechnologyList);
    }
}
