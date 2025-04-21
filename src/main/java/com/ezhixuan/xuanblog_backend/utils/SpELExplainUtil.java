package com.ezhixuan.xuanblog_backend.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * spel 解释器
 */
public class SpELExplainUtil {

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    public String explain(String explain, ProceedingJoinPoint point) {
        if (!StringUtils.hasText(explain) || !explain.contains("#")) {
            return explain;
        }

        Expression parseExpression = parser.parseExpression(explain);
        MethodSignature method = (MethodSignature) point.getSignature();
        String[] paramNames = nameDiscoverer.getParameterNames(method.getMethod());
        if (paramNames == null || paramNames.length == 0) {
            return explain;
        }
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = point.getArgs();
        for(int i = 0 ; i < args.length ; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return Objects.requireNonNull(parseExpression.getValue(context)).toString();
    }
}
