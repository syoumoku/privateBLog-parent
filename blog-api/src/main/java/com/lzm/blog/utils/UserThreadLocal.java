package com.lzm.blog.utils;

import com.lzm.blog.dao.pojo.SysUser;

public class UserThreadLocal {

    private UserThreadLocal() {}


    //线程变量隔离
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser) {
        LOCAL.set(sysUser);
    }

    public static SysUser get() {
        return LOCAL.get();
    }


    public static void remove() {
        //防止内存泄露
        LOCAL.remove();
    }
}
