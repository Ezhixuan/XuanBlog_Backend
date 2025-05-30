package com.ezhixuan.xuanblog_backend.domain.entity.message;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 消息任务表
 * @TableName message_task
 */
@TableName(value ="message_task")
@Data
public class MessageTask implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 任务唯一ID
     */
    @TableField(value = "task_id")
    private String taskId;

    /**
     * 消息类型
     */
    @TableField(value = "message_type")
    private String messageType;

    /**
     * 业务ID
     */
    @TableField(value = "business_id")
    private String businessId;

    /**
     * 业务类型
     */
    @TableField(value = "business_type")
    private String businessType;

    /**
     * 发送者
     */
    @TableField(value = "sender")
    private String sender;

    /**
     * 接收者列表(JSON)
     */
    @TableField(value = "recipients")
    private String recipients;

    /**
     * 消息主题
     */
    @TableField(value = "subject")
    private String subject;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 扩展数据
     */
    @TableField(value = "extra_data")
    private Object extraData;

    /**
     * 优先级(1-10，数字越大优先级越高)
     */
    @TableField(value = "priority")
    private Integer priority;

    /**
     * 状态(PENDING/PROCESSING/SUCCESS/FAILED/CANCELLED)
     */
    @TableField(value = "status")
    private String status;

    /**
     * 重试次数
     */
    @TableField(value = "retry_count")
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    @TableField(value = "max_retry")
    private Integer maxRetry;

    /**
     * 下次重试时间
     */
    @TableField(value = "next_retry_time")
    private Date nextRetryTime;

    /**
     * 错误信息
     */
    @TableField(value = "error_message")
    private String errorMessage;

    /**
     * 执行时间
     */
    @TableField(value = "execute_time")
    private LocalDateTime executeTime;

    /**
     * 完成时间
     */
    @TableField(value = "complete_time")
    private LocalDateTime completeTime;

    /**
     * 过期时间
     */
    @TableField(value = "expired_time")
    private LocalDateTime expiredTime;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private LocalDateTime updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
