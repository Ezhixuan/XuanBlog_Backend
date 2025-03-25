package org.ezhixuan.xuan_picture_backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ezhixuan
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -4920382884094083728L;

    private Long id;
}
