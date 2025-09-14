package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.entity.Tag;
import com.ezhixuan.blog.service.TagService;
import com.ezhixuan.blog.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author ezhixuan
* @description 针对表【tag(文章标签)】的数据库操作Service实现
* @createDate 2025-09-13 10:40:23
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




