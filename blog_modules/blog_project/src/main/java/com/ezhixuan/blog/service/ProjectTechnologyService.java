package com.ezhixuan.blog.service;

import com.ezhixuan.blog.domain.entity.ProjectTechnology;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ezhixuan
* @description 针对表【project_technology】的数据库操作Service
* @createDate 2025-07-05 22:13:11
*/
public interface ProjectTechnologyService extends IService<ProjectTechnology> {

    /**
     * 根据技术栈获取 id
     * @author Ezhixuan
     * @param technology 技术栈名称
     * @return 技术栈 id
     */
    Long getIdByName(String technology);

    /**
     * 根据 id 获取名称
     * @param list  id
     * @return 名称
     */
    List<String> getName(List<?> list);

    /**
     * 新增并返回拼接后的 ids
     * @author Ezhixuan
     * @param technologies 技术栈名称
     * @return ids
     */
    List<Long> saveAll(List<String> technologies);
}
