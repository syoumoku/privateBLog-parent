package com.lzm.blog.common.aop;


import com.alibaba.fastjson.JSON;
import com.lzm.blog.utils.HttpContextUtils;
import com.lzm.blog.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect// the relation between notification and pointcut
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.lzm.blog.common.aop.LogAnnotation)")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long begintime = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - begintime;
        recordLog(point, time);
        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("=====================log start================================");
        log.info("module:{}",logAnnotation.module());
        log.info("operation:{}",logAnnotation.operator());

        //request method name
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}",className + "." + methodName + "()");

//        //request arguments
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}",params);

        //request set IP address
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));


        log.info("execute time : {} ms",time);
        log.info("=====================log end================================");
    }

}
