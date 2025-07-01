package com.ezhixuan.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezhixuan.blog.service.MemoCardService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/memo")
class MemoController {

    @Resource
    private MemoCardService cardService;



}
