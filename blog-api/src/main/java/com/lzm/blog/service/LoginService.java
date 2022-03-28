package com.lzm.blog.service;

import com.lzm.blog.dao.pojo.SysUser;
import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.params.LoginPrams;

public interface LoginService {

    Result login(LoginPrams loginPrams);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginPrams loginPrams);
}
