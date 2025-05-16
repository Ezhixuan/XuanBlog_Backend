package com.ezhixuan.xuanblog_backend.domain.vo;

import lombok.Data;

@Data
public class MemoCardVO {

    private Long id;

    private Long deckId;

    private String deckName;

    private String front;

    private String back;
}
