package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.Technology;
import com.ezhixuan.blog.mapper.TechnologyMapper;
import com.ezhixuan.blog.service.TechnologyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author ezhixuan
 * @description 针对表【project_technology】的数据库操作Service实现
 * @createDate 2025-07-05 22:13:11
 */
@Service
public class TechnologyServiceImpl extends ServiceImpl<TechnologyMapper, Technology>
    implements TechnologyService {

  /**
   * 根据技术栈获取 id
   *
   * @param technology 技术栈名称
   * @return 技术栈 id
   * @author Ezhixuan
   */
  @Override
  public Long getIdByName(String technology) {
    LambdaQueryWrapper<Technology> lqw = queryWrapper(technology);
    Technology one = getOne(lqw);
    if (Objects.nonNull(one)) {
      return one.getId();
    }
    one = new Technology();
    one.setName(technology);
    save(one);
    return one.getId();
  }

  private LambdaQueryWrapper<Technology> queryWrapper(String technology) {
    return new LambdaQueryWrapper<Technology>().eq(Technology::getName, technology);
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
    return listObjs(
        Wrappers.<Technology>lambdaQuery().select(Technology::getName).in(Technology::getId, ids));
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
