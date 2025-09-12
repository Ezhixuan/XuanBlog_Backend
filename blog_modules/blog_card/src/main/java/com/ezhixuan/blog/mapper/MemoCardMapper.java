package com.ezhixuan.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ezhixuan.blog.domain.dto.MemoQueryDTO;
import com.ezhixuan.blog.domain.entity.MemoCard;
import com.ezhixuan.blog.domain.vo.MemoCardVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ezhixuan
 * @description 针对表【memo_card】的数据库操作Mapper
 * @createDate 2025-05-14 11:13:15
 * @Entity com.ezhixuan.blog.domain.entity.MemoCard
 */
public interface MemoCardMapper extends BaseMapper<MemoCard> {

    /**
     * 优化的分页查询，使用JOIN避免N+1问题
     *
     * @param page 分页对象
     * @param queryDTO 查询条件
     * @return 卡片VO列表
     */
    @Select("SELECT c.*, d.name as deck_name " +
            "FROM memo_card c " +
            "LEFT JOIN memo_decks d ON c.deck_id = d.id " +
            "WHERE #{queryDTO.deckId} IS NULL OR c.deck_id = #{queryDTO.deckId} " +
            "ORDER BY c.next_review_date DESC")
    List<MemoCardVO> selectCardPageWithDeckName(IPage<MemoCardVO> page, @Param("queryDTO") MemoQueryDTO queryDTO);

    /**
     * 优化的随机卡片选择，使用数据库随机函数
     *
     * @param deckId 卡片集ID（可选）
     * @param limit 限制数量
     * @return 随机卡片列表
     */
    @Select("SELECT c.*, d.name as deck_name " +
            "FROM memo_card c " +
            "LEFT JOIN memo_decks d ON c.deck_id = d.id " +
            "WHERE c.next_review_date <= CURRENT_DATE " +
            "AND (#{deckId} IS NULL OR c.deck_id = #{deckId}) " +
            "ORDER BY RAND() " +
            "LIMIT #{limit}")
    List<MemoCardVO> selectRandomCardsForReview(@Param("deckId") Long deckId, @Param("limit") int limit);

    /**
     * 统计需要复习的卡片数量
     *
     * @param deckId 卡片集ID（可选）
     * @return 需要复习的卡片数量
     */
    @Select("SELECT COUNT(*) FROM memo_card " +
            "WHERE next_review_date <= CURRENT_DATE " +
            "AND (#{deckId} IS NULL OR deck_id = #{deckId})")
    int countCardsForReview(@Param("deckId") Long deckId);

    /**
     * 批量更新复习日期
     *
     * @param cardIds 卡片ID列表
     * @param nextReviewDate 新的复习日期
     * @return 更新的行数
     */
    int batchUpdateReviewDate(@Param("cardIds") List<Long> cardIds,
                             @Param("nextReviewDate") LocalDateTime nextReviewDate);
}




