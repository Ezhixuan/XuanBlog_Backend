package com.ezhixuan.xuanblog_backend.utils;

import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import cn.dev33.satoken.stp.StpUtil;

/**
 * 封装一下一些问题
 * 用到再加
 */
@Component
public class IStpUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static String getActiveProfile() {
        if (applicationContext != null) {
            return applicationContext.getEnvironment().getActiveProfiles()[0];
        }
        return null;
    }

    @Value("${spring.profiles.active}")
    private String env;

    public static long getLoginIdAsLong() {
        String activeProfile = getActiveProfile();
        if (Objects.equals(activeProfile, "dev")) {
            return 1;
        }
        return StpUtil.getLoginIdAsLong();
    }

}
