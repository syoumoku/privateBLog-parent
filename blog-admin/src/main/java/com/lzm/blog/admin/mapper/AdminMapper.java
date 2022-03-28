package com.lzm.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzm.blog.admin.pojo.Admin;
import com.lzm.blog.admin.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    @Select("select p.id as id, name, path, description from ms_permission as p left join ms_admin_permission on p.id = permission_id where admin_id = #{adminId}")
    List<Permission> getPermissionByUserId(Long adminId);
}
