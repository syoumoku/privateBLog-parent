package com.lzm.blog.service;

import com.lzm.blog.dao.pojo.SysUser;
import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.UserVo;

public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result getUserInfoByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long id);
}
