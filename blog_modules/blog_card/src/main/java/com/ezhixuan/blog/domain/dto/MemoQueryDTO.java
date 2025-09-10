package com.ezhixuan.blog.domain.dto;

import com.ezhixuan.blog.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemoQueryDTO extends PageRequest {

    private Integer pageNo = 1;

    private Integer pageSize = 9;

    private Long deckId;
}
