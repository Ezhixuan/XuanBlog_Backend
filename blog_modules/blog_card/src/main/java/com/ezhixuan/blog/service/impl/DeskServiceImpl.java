package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.Desk;
import com.ezhixuan.blog.mapper.DeskMapper;
import com.ezhixuan.blog.service.DeskService;
import org.springframework.stereotype.Service;

/**
 * @author ezhixuan
 * @description 针对表【desk(卡片分组（卡片集）)】的数据库操作Service实现
 * @createDate 2025-10-08 17:40:18
 */
@Service
public class DeskServiceImpl extends ServiceImpl<DeskMapper, Desk> implements DeskService {}
