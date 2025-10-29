package com.ezhixuan.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ezhixuan.blog.controller.dto.CardQueryDTO;
import com.ezhixuan.blog.controller.dto.CardTestDTO;
import com.ezhixuan.blog.controller.vo.CardVo;
import com.ezhixuan.blog.domain.entity.Card;
import com.ezhixuan.blog.domain.entity.Deck;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.mapper.CardMapper;
import com.ezhixuan.blog.service.CardService;
import com.ezhixuan.blog.service.DeckService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author ezhixuan
 * @description 针对表【card(记忆卡片)】的数据库操作Service实现
 * @createDate 2025-10-08 17:40:18
 */
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {

  @Resource private DeckService deckService;

  /**
   * 分页查询卡片列表
   *
   * @param queryDTO 卡片查询条件DTO
   * @return 卡片分页结果
   */
  @Override
  public IPage<CardVo> list(CardQueryDTO queryDTO) {
    if (Objects.isNull(queryDTO)) {
      return new Page<>();
    }

    IPage<Card> paged = page(queryDTO.toPage(), getQueryWrapper(queryDTO));
    List<Card> cardList = paged.getRecords();
    if (isEmpty(cardList)) {
      return new Page<>();
    }
    IPage<CardVo> converted = paged.convert(CardVo::fromEntity);
    List<CardVo> cardVoList = converted.getRecords();
    List<Long> parentIds = cardList.stream().map(Card::getParentId).toList();
    List<Long> deckIds = cardList.stream().map(Card::getDeckId).toList();

    // 关联查询父卡片正面内容
    if (!isEmpty(parentIds)) {
      List<Card> parentCardList = listByIds(parentIds);
      if (!isEmpty(parentCardList)) {
        Map<Long, String> idToFrontMap =
            parentCardList.stream().collect(Collectors.toMap(Card::getId, Card::getFront));
        cardVoList.forEach(
            card -> {
              if (nonNull(card.getParentId())) {
                card.setParentFront(idToFrontMap.get(card.getParentId()));
              }
            });
      }
    }

    // 关联查询桌面名称
    if (!isEmpty(deckIds)) {
      List<Deck> deckList = deckService.listByIds(deckIds);
      if (!isEmpty(deckList)) {
        Map<Long, String> idToNameMap =
            deckList.stream().collect(Collectors.toMap(Deck::getId, Deck::getName));
        cardVoList.forEach(
            card -> {
              if (nonNull(card.getDeckId())) {
                card.setDeckName(idToNameMap.get(card.getDeckId()));
              }
            });
      }
    }
    return converted;
  }

  /**
   * 执行卡片测试
   *
   * @param testDTO 卡片测试DTO
   */
  @Override
  public void doTest(CardTestDTO testDTO) {
    if (isNull(testDTO) || isNull(testDTO.getTestId())) {
      return;
    }

    Card card = getById(testDTO.getTestId());
    if (isNull(card)) {
      throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
    }

    // 没有想起来,重置次数
    if (!testDTO.isSuccess()) {
      reinitialize(card);
      return;
    }

    long reps = card.getReps() + 1;
    double EF = 2.5;
    double interval;
    if (reps == 1) {
      interval = 1;
    } else if (reps == 2) {
      interval = 6;
    } else {
      double q = getQuality(testDTO.getUseTime());
      EF = getEF(EF, q);
      interval = getInterval(card.getInterval(), EF);
    }

    // 更新数据
    card.setReps(reps);
    card.setInterval(interval);
    card.setEF(EF);
    card.setNextTime(LocalDate.now().plusDays(Math.round(interval)));
    updateById(card);
  }

  /**
   * 分页查询测试卡片VO列表
   *
   * @param queryDTO 查询条件DTO
   * @return 卡片VO列表
   */
  @Override
  public List<CardVo> pageTestCardVo(CardQueryDTO queryDTO) {
    List<Card> cards = pageTestCard(queryDTO);
    return cards.stream().map(CardVo::fromEntity).toList();
  }

  /**
   * 分页查询测试卡片
   *
   * @param queryDTO 查询条件DTO
   * @return 卡片列表
   */
  private List<Card> pageTestCard(CardQueryDTO queryDTO) {
    queryDTO.setTime(LocalDateTime.now());
    LambdaQueryWrapper<Card> lqw = new LambdaQueryWrapper<>();
    lqw.eq(nonNull(queryDTO.getDeckId()), Card::getDeckId, queryDTO.getDeckId());
    lqw.le(Card::getNextTime, queryDTO.getTime());
    lqw.last("limit 30");
    List<Card> cardList = list(lqw);
    if (cardList.isEmpty()) {
      return new ArrayList<>();
    } else if (cardList.size() <= 10) {
      return cardList;
    }
    // 随机抽取 10 条
    Collections.shuffle(cardList);
    return cardList.subList(0, 10);
  }

  /**
   * 重新初始化卡片参数
   *
   * @param card 卡片实体
   */
  private void reinitialize(Card card) {
    if (isNull(card) || isNull(card.getId())) {
      return;
    }
    card.setInterval(1.0);
    card.setEF(2.5);
    card.setLapses(card.getLapses() + 1);
    card.setReps(0L);
    card.setNextTime(LocalDate.now().plusDays(1));
    updateById(card);
  }

  /**
   * 根据使用时间获取质量系数
   *
   * @param useTime 使用时间
   * @return 质量系数
   */
  private double getQuality(long useTime) {
    return useTime < 10L ? 5 : 4;
  }

  /**
   * 计算EF值(遗忘因子)
   *
   * @param oldEF 旧的EF值
   * @param q 质量系数
   * @return 新的EF值
   */
  private double getEF(double oldEF, double q) {
    double delta = 0.1 - (5 - q) * (0.08 + (5 - q) * 0.02);
    return Math.max(1.3, oldEF + delta);
  }

  /**
   * 计算间隔天数
   *
   * @param oldIv 旧的间隔天数
   * @param EF 遗忘因子
   * @return 新的间隔天数
   */
  private double getInterval(double oldIv, double EF) {
    return Math.max(1, Math.round(oldIv * EF * 100) / 100.0);
  }

  /**
   * 构建查询条件
   *
   * @param queryDTO 查询条件DTO
   * @return 查询条件包装器
   */
  private LambdaQueryWrapper<Card> getQueryWrapper(CardQueryDTO queryDTO) {
    LambdaQueryWrapper<Card> lqw = new LambdaQueryWrapper<>();
    List<Long> deckIds = deckService.listIdByUser();
    deckIds.add(0L);
    lqw.eq(nonNull(queryDTO.getId()), Card::getId, queryDTO.getId());
    lqw.eq(nonNull(queryDTO.getDeckId()), Card::getDeckId, queryDTO.getDeckId());
    lqw.in(Card::getDeckId, deckIds);
    lqw.orderByDesc(Card::getNextTime);
    return lqw;
  }
}
