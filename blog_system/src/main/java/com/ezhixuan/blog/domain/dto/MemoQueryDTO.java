package com.ezhixuan.blog.domain.dto;

import com.ezhixuan.blog.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemoQueryDTO extends PageRequest {

    private int current = 1;

    private int pageSize = 9;

    private Long deckId;
}
