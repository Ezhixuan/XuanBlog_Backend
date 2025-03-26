package com.ezhixuan.xuanblog_backend.aop;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson2.JSON;
import com.ezhixuan.xuanblog_backend.domain.InterfaceLog;
import com.ezhixuan.xuanblog_backend.domain.constant.InterfaceLogStatusConstant;
import com.ezhixuan.xuanblog_backend.service.InterfaceLogService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
@Order(0)
public class InterfaceLogInterceptor {

    @Resource
    private InterfaceLogService logService;

    private static final Long TIMEOUT_THRESHOLD = 5000L;

    @SneakyThrows
    @Around("execution(* com.ezhixuan.xuanblog_backend.controller..*.*(..))")
    public Object logAspect(ProceedingJoinPoint joinPoint) {
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setRequestTime(new Date());
        interfaceLog.setInterfaceName(joinPoint.getSignature().toShortString());
        interfaceLog.setRequestParams(JSON.toJSONString(joinPoint.getArgs()));
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            String remoteAddr = request.getRemoteAddr();
            interfaceLog.setClientIp(remoteAddr);
            /*
              todo Ezhixuan : user
             */
        }

        long startTime = System.currentTimeMillis();
        try {
            Object res = joinPoint.proceed();
            interfaceLog.setResponseData(JSON.toJSONString(res));
            interfaceLog.setStatus(InterfaceLogStatusConstant.SUCCESS);
            return res;
        }catch (Exception e) {
            interfaceLog.setStatus(InterfaceLogStatusConstant.FAILURE);
            throw e;
        }finally {
            long endTime = System.currentTimeMillis();
            long exTime = endTime - startTime;
            /*
            如果此时方法执行成功并返回了，但是整体方法执行超过了超时阈值，整体接口是需要进行优化的
             */
            if (exTime >= TIMEOUT_THRESHOLD) {
                interfaceLog.setStatus(InterfaceLogStatusConstant.TIMEOUT);
            }
            interfaceLog.setExecutionTime(new BigDecimal(exTime));
            logService.save(interfaceLog);
        }
    }
}
