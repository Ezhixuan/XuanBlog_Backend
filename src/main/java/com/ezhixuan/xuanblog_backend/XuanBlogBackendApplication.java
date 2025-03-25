package com.ezhixuan.xuanblog_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ezhixuan.xuanblog_backend.mapper")
public class XuanBlogBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(XuanBlogBackendApplication.class, args);
    }

}
