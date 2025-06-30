package com.ezhixuan.blog.domain.entity.message;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * WebSocket会话表
 * @TableName websocket_session
 */
@TableName(value ="websocket_session")
@Data
public class WebsocketSession implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话ID
     */
    @TableField(value = "session_id")
    private String sessionId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户类型
     */
    @TableField(value = "user_type")
    private String userType;

    /**
     * 客户端IP
     */
    @TableField(value = "client_ip")
    private String clientIp;

    /**
     * 用户代理
     */
    @TableField(value = "user_agent")
    private String userAgent;

    /**
     * 状态(ACTIVE/INACTIVE)
     */
    @TableField(value = "status")
    private String status;

    /**
     * 最后心跳时间
     */
    @TableField(value = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

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
