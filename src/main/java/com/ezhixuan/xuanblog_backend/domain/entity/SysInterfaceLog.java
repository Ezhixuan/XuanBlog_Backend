package com.ezhixuan.xuanblog_backend.domain.entity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 接口调用日志表
 * @TableName interface_log
 */
@TableName(value ="sys_interface_log")
@Data
public class SysInterfaceLog implements Serializable {
    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 请求时间
     */
    @TableField(value = "request_time")
    private Date requestTime;

    /**
     * 接口名称
     */
    @TableField(value = "interface_name")
    private String interfaceName;

    /**
     * 请求参数（JSON格式）
     */
    @TableField(value = "request_params")
    private String requestParams;

    /**
     * 返回数据（JSON格式）
     */
    @TableField(value = "response_data")
    private String responseData;

    /**
     * 调用状态
     */
    @TableField(value = "status")
    private Object status;

    /**
     * 错误信息（失败时记录）
     */
    @TableField(value = "error_info")
    private String errorInfo;

    /**
     * 执行耗时（秒）
     */
    @TableField(value = "execution_time")
    private BigDecimal executionTime;

    /**
     * 调用方IP地址
     */
    @TableField(value = "client_ip")
    private String clientIp;

    /**
     * 用户ID（可选）
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 请求唯一标识（可选）
     */
    @TableField(value = "request_id")
    private String requestId;

    /**
     * 记录创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
