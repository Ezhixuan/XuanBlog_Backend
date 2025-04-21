package com.ezhixuan.xuanblog_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class XuanBlogBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(XuanBlogBackendApplication.class, args);
    }

}
