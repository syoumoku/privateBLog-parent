package com.lzm.blog.handler;

import com.lzm.blog.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//对有@Controller注解的方法进行拦截处理
@ControllerAdvice

public class AllExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody// return json file
    public Result doException(Exception ex) {
        ex.printStackTrace();
        return Result.fail(-999, "system exception");
    }
}
