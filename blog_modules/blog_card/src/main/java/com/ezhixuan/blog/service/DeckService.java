package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.domain.entity.Deck;
import java.util.List;

/**
 * @author ezhixuan
 * @description 针对表【deck(卡片分组（卡片集）)】的数据库操作Service
 * @createDate 2025-10-08 17:40:18
 */
public interface DeckService extends IService<Deck> {

  /**
   * 获取用户卡片集列表
   *
   * @return 卡片集列表
   */
  List<Deck> listByUser();

  /**
   * 获取用户卡片集ID列表
   *
   * @return 卡片集ID列表
   */
  List<Long> listIdByUser();
}
