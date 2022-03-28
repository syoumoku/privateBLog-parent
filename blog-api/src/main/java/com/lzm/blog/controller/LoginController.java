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
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginPrams loginPrams) {
        return loginService.login(loginPrams);
    }
}
