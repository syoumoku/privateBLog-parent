package com.lzm.blog.admin.service;

import com.lzm.blog.admin.mapper.PermissionMapper;
import com.lzm.blog.admin.model.params.PageParam;
import com.lzm.blog.admin.pojo.Permission;
import com.lzm.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public interface PermissionService {


    Result listPermission(PageParam pageParam);

    Result add(Permission permission);

    Result update(Permission permission);

    Result delete(Long id);
}
