package com.ezhixuan.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.domain.entity.Deck;
import com.ezhixuan.blog.mapper.DeckMapper;
import com.ezhixuan.blog.service.DeckService;
import com.ezhixuan.blog.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ezhixuan
 * @description 针对表【deck(卡片分组（卡片集）)】的数据库操作Service实现
 * @createDate 2025-10-08 17:40:18
 */
@Service
public class DeckServiceImpl extends ServiceImpl<DeckMapper, Deck> implements DeckService {

  @Resource private UserService userService;

  @Override
  public List<Deck> listByUser() {
    long loginId = -1L;
    try {
      loginId = StpUtil.getLoginIdAsLong();
    } catch (Exception ignored) {
    }

    // 存在登录用户
    if (loginId != -1L && userService.isAdmin(loginId)) {
      return list();
    }

    return list(Wrappers.<Deck>lambdaQuery().eq(Deck::isPrivateDeck, false));
  }

  @Override
  public List<Long> listIdByUser() {
    long loginId = -1L;
    try {
      loginId = StpUtil.getLoginIdAsLong();
    } catch (Exception ignored) {
    }

    if (loginId != -1L && userService.isAdmin(loginId)) {
      return listObjs(Wrappers.<Deck>lambdaQuery().select(Deck::getId));
    }
    return listObjs(
        Wrappers.<Deck>lambdaQuery().eq(Deck::isPrivateDeck, false).select(Deck::getId));
  }
}
