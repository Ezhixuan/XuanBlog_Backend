package com.ezhixuan.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ezhixuan.blog.controller.dto.CardQueryDTO;
import com.ezhixuan.blog.controller.dto.CardTestDTO;
import com.ezhixuan.blog.controller.vo.CardVo;
import com.ezhixuan.blog.domain.entity.Card;

import java.util.List;

/**
 * @author ezhixuan
 * @description 针对表【card(记忆卡片)】的数据库操作Service
 * @createDate 2025-10-08 17:40:18
 */
public interface CardService extends IService<Card> {

  /**
   * 查询卡片列表
   *
   * @param queryDTO 查询参数
   * @return 卡片列表
   */
  IPage<CardVo> list(CardQueryDTO queryDTO);

  /**
   * 测试卡片
   *
   * @param testDTO 测试参数
   */
  void doTest(CardTestDTO testDTO);

  /**
   * 测试卡片列表
   *
   * @param queryDTO 测试参数
   * @return 测试卡片列表
   */
  List<CardVo> pageTestCardVo(CardQueryDTO queryDTO);
}
