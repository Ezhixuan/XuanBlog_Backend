package com.ezhixuan.xuanblog_backend.common;

import java.io.Serializable;

import lombok.Data;

/**
 * @author ezhixuan
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -4920382884094083728L;

    private Long id;
}
