package com.garwer.springboot.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Garwer
 * @Date: 19/3/5 下午11:36
 * @Version 1.0
 * 执行后织入
 */

@Aspect //将类定义为切面类
@Component
public class LogAspect {
    @Pointcut("execution(public * com.garwer.springboot.aop.web..*.*(..))") //定义切入点
    public void webLog() {}

    @Before(value = "webLog()")
    public void doBefore(JoinPoint joinpoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        System.out.println("url:" + request.getRequestURI());
        System.out.println("ip:" + request.getRemoteAddr());
    }

    @AfterReturning(pointcut = "webLog()")
    public void doAfterReturning(JoinPoint joinPoint) {
        System.out.println("response" + joinPoint);
    }
}
