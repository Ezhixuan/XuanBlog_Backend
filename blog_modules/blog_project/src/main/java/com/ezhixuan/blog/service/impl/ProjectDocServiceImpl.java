package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.controller.dto.ProjectDocCreateDTO;
import com.ezhixuan.blog.controller.dto.ProjectDocQueryDTO;
import com.ezhixuan.blog.controller.dto.ProjectDocUpdateDTO;
import com.ezhixuan.blog.domain.entity.ProjectDoc;
import com.ezhixuan.blog.mapper.ProjectDocMapper;
import com.ezhixuan.blog.service.ProjectDocService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author ezhixuan
 * @description 针对表【project_doc(项目文档)】的数据库操作Service实现
 * @createDate 2025-09-18 14:39:11
 */
@Service
public class ProjectDocServiceImpl extends ServiceImpl<ProjectDocMapper, ProjectDoc>
    implements ProjectDocService {

  @Override
  public ProjectDoc create(ProjectDocCreateDTO createDTO) {
    ProjectDoc projectDoc = new ProjectDoc();
    BeanUtils.copyProperties(createDTO, projectDoc);
    projectDoc.setCreateTime(LocalDateTime.now());
    this.save(projectDoc);
    return projectDoc;
  }

  @Override
  public ProjectDoc update(ProjectDocUpdateDTO updateDTO) {
    ProjectDoc projectDoc = new ProjectDoc();
    BeanUtils.copyProperties(updateDTO, projectDoc);
    this.updateById(projectDoc);
    return projectDoc;
  }

  @Override
  public Boolean deleteById(Long id) {
    return this.removeById(id);
  }

  @Override
  public IPage<ProjectDoc> pageByDTO(ProjectDocQueryDTO queryDTO) {
    LambdaQueryWrapper<ProjectDoc> queryWrapper = new LambdaQueryWrapper<>();
    if (queryDTO.getProjectId() != null) {
      queryWrapper.eq(ProjectDoc::getProjectId, queryDTO.getProjectId());
    }
    if (queryDTO.getTitle() != null && !queryDTO.getTitle().isEmpty()) {
      queryWrapper.like(ProjectDoc::getTitle, queryDTO.getTitle());
    }

    return this.page(queryDTO.toPage(), queryWrapper);
  }
}
