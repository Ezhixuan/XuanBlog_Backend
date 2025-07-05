package com.ezhixuan.blog.service.impl;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.ProjectTechnology;
import com.ezhixuan.blog.mapper.ProjectTechnologyMapper;
import com.ezhixuan.blog.service.ProjectTechnologyService;

/**
 * @author ezhixuan
 * @description 针对表【project_technology】的数据库操作Service实现
 * @createDate 2025-07-05 22:13:11
 */
@Service
public class ProjectTechnologyServiceImpl extends ServiceImpl<ProjectTechnologyMapper, ProjectTechnology>
    implements ProjectTechnologyService {

    /**
     * 根据技术栈获取 id
     *
     * @param technology 技术栈名称
     * @return 技术栈 id
     * @author Ezhixuan
     */
    @Override
    public Long getIdByName(String technology) {
        LambdaQueryWrapper<ProjectTechnology> lqw = queryWrapper(technology);
        ProjectTechnology one = getOne(lqw);
        if (Objects.nonNull(one)) {
            return one.getId();
        }
        one = new ProjectTechnology();
        one.setName(technology);
        save(one);
        return one.getId();
    }

    private LambdaQueryWrapper<ProjectTechnology> queryWrapper(String technology) {
        return new LambdaQueryWrapper<ProjectTechnology>().eq(ProjectTechnology::getName, technology);
    }

    /**
     * 根据 id 获取名称
     *
     * @param ids id
     * @return 名称
     */
    @Override
    public List<String> getName(List<?> ids) {
        if (isEmpty(ids)) {
            return emptyList();
        }
        return listObjs(Wrappers.<ProjectTechnology>lambdaQuery().select(ProjectTechnology::getName).in(ProjectTechnology::getId, ids));
    }

    /**
     * 新增并返回拼接后的 ids
     *
     * @param technologies 技术栈名称
     * @return ids
     * @author Ezhixuan
     */
    @Override
    public List<Long> saveAll(List<String> technologies) {
        if (isEmpty(technologies)) {
            return emptyList();
        }
        return technologies.stream().distinct().map(this::getIdByName).toList();
    }
}
