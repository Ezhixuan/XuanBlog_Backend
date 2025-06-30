package com.ezhixuan.blog.domain.entity.message;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 消息执行日志表
 * @TableName message_execution_log
 */
@TableName(value ="message_execution_log")
@Data
public class MessageExecutionLog implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务ID
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 执行ID
     */
    @TableField(value = "execution_id")
    private String executionId;

    /**
     * 执行状态(PROCESSING/SUCCESS/FAILED)
     */
    @TableField(value = "status")
    private String status;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 执行时长(毫秒)
     */
    @TableField(value = "duration")
    private Long duration;

    /**
     * 错误信息
     */
    @TableField(value = "error_message")
    private String errorMessage;

    /**
     * 执行线程
     */
    @TableField(value = "worker_thread")
    private String workerThread;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
