package com.lzm.blog.controller;

import com.lzm.blog.service.LoginService;
import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.params.LoginPrams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;


    @PostMapping
    public Result register(@RequestBody LoginPrams loginPrams) {
        //sso 单点登录

        return loginService.register(loginPrams);
    }
}
