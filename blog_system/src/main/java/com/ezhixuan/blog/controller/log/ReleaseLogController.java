package com.ezhixuan.blog.controller.log;

import com.ezhixuan.blog.common.PageResponse;
import com.ezhixuan.blog.common.R;
import com.ezhixuan.blog.controller.log.dto.ReleaseLogCreateDTO;
import com.ezhixuan.blog.controller.log.dto.ReleaseLogQueryDTO;
import com.ezhixuan.blog.controller.log.dto.ReleaseLogUpdateDTO;
import com.ezhixuan.blog.controller.log.vo.ReleaseLogVO;
import com.ezhixuan.blog.domain.entity.ReleaseLog;
import com.ezhixuan.blog.entity.BaseResponse;
import com.ezhixuan.blog.exception.BusinessException;
import com.ezhixuan.blog.exception.ErrorCode;
import com.ezhixuan.blog.service.ReleaseLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 发布日志控制器
 *
 * @author ezhixuan
 */
@RestController
@RequestMapping("/releaseLog")
@RequiredArgsConstructor
@Tag(name = "ReleaseLogController", description = "发布日志的增删改查接口")
public class ReleaseLogController {

  private final ReleaseLogService releaseLogService;

  /**
   * 创建发布日志
   *
   * @param createDTO 创建参数
   * @return 创建结果
   */
  @PostMapping
  @Operation(summary = "创建发布日志", description = "创建一条新的发布日志记录")
  public BaseResponse<Long> createReleaseLog(@Valid @RequestBody ReleaseLogCreateDTO createDTO) {
    ReleaseLog releaseLog = createDTO.toEntity();
    releaseLogService.save(releaseLog);
    return R.success(releaseLog.getId());
  }

  /**
   * 删除发布日志
   *
   * @param id 发布日志ID
   * @return 删除结果
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "删除发布日志", description = "根据ID删除发布日志")
  public BaseResponse<Void> deleteReleaseLog(@PathVariable Long id) {
    ReleaseLog releaseLog = releaseLogService.getById(id);
    if (Objects.isNull(releaseLog)) {
      throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
    }
    releaseLogService.removeById(id);
    return R.success();
  }

  /**
   * 更新发布日志
   *
   * @param updateDTO 更新参数
   * @return 更新结果
   */
  @PutMapping
  @Operation(summary = "更新发布日志", description = "更新发布日志信息")
  public BaseResponse<Void> updateReleaseLog(@Valid @RequestBody ReleaseLogUpdateDTO updateDTO) {
    ReleaseLog releaseLog = updateDTO.toEntity();
    releaseLogService.updateById(releaseLog);
    return R.success();
  }

  /**
   * 根据ID查询发布日志
   *
   * @param id 发布日志ID
   * @return 发布日志信息
   */
  @GetMapping("/{id}")
  @Operation(summary = "查询发布日志", description = "根据ID查询发布日志详情")
  public BaseResponse<ReleaseLogVO> getReleaseLogById(
      @Parameter(description = "发布日志ID") @PathVariable Long id) {
    ReleaseLog releaseLog = releaseLogService.getById(id);
    ReleaseLogVO vo = ReleaseLogVO.fromEntity(releaseLog);
    return R.success(vo);
  }

  /**
   * 分页查询发布日志列表
   *
   * @param queryDTO 查询参数
   * @return 分页结果
   */
  @GetMapping("/page")
  @Operation(summary = "分页查询发布日志", description = "分页查询发布日志列表，按创建时间倒序排序")
  public PageResponse<ReleaseLog> pageReleaseLogList(@Valid ReleaseLogQueryDTO queryDTO) {
    return R.list(releaseLogService.pageReleaseLogList(queryDTO));
  }
}
