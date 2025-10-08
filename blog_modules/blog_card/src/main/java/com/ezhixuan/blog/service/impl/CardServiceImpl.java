package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.Card;
import com.ezhixuan.blog.mapper.CardMapper;
import com.ezhixuan.blog.service.CardService;
import org.springframework.stereotype.Service;

/**
 * @author ezhixuan
 * @description 针对表【card(记忆卡片)】的数据库操作Service实现
 * @createDate 2025-10-08 17:40:18
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {}
