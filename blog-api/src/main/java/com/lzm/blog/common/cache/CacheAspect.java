package com.lzm.blog.common.cache;


import com.alibaba.fastjson.JSON;
import com.lzm.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.PriorityQueue;

@Aspect
@Component
@Slf4j
public class CacheAspect {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.lzm.blog.common.cache.Cache)")
    public void pt(){}

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp){
        try{

            Signature signature = pjp.getSignature();
            //get class name
            String className = pjp.getTarget().getClass().getSimpleName();
            //get method name
            String methodName = signature.getName();

            Class[] parameterTypes = new Class[pjp.getArgs().length];
            //get args list
            Object[] args = pjp.getArgs();

            String params = "";
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    //to json
                    params += JSON.toJSONString(args[i]);
                    //get parameters type
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }

            //encrypt the params in order to avoid overlength or cannot get the char during transmission
            if (StringUtils.isNotEmpty(params)) {
                params = DigestUtils.md5Hex(params);
            }

            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);

            //get the annotation
            Cache annotation = method.getAnnotation(Cache.class);



            // cache time
            long expire = annotation.expire();
            // cache name
            String name = annotation.name();
            //get from redis
            String redisKey  = name + "::" + className + "::" + methodName + "::" + params;
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            if (StringUtils.isNotEmpty(redisValue)) {
                log.info("Load the value from cache~~~,{},{}", className, methodName);
                return JSON.parseObject(redisValue, Result.class);
            }
            Object proceed = pjp.proceed();
            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(proceed), Duration.ofMillis(expire));
            log.info("Store the value into cache~~~{},{}", className, methodName);
            return proceed;
        } catch (Throwable e){
            e.printStackTrace();
        }
        return Result.fail(-999, "System error");
    }

}
