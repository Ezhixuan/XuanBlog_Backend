package com.ezhixuan.xuanblog_backend.aop;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson2.JSON;
import com.ezhixuan.xuanblog_backend.domain.InterfaceLog;
import com.ezhixuan.xuanblog_backend.service.InterfaceLogService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class InterfaceLogInterceptor {

    @Resource
    private InterfaceLogService logService;

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
            /**
             * todo Ezhixuan : user
             */
        }

        long startTime = System.currentTimeMillis();
        try {
            Object res = joinPoint.proceed();
            interfaceLog.setResponseData(JSON.toJSONString(res));
            interfaceLog.setStatus("success");
            return res;
        }catch (Exception e) {
            interfaceLog.setStatus("failure");
            throw e;
        }finally {
            long endTime = System.currentTimeMillis();
            long exTime = endTime - startTime;
            interfaceLog.setExecutionTime(new BigDecimal(exTime));
            logService.save(interfaceLog);
        }
    }
}
