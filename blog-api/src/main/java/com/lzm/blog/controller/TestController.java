package com.lzm.blog.controller;


import com.lzm.blog.dao.pojo.SysUser;
import com.lzm.blog.utils.UserThreadLocal;
import com.lzm.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
