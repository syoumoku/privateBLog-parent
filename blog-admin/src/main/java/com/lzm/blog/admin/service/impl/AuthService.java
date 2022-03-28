package com.lzm.blog.admin.service.impl;


import com.lzm.blog.admin.pojo.Admin;
import com.lzm.blog.admin.pojo.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service


public class AuthService {

    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest request, Authentication authentication) {
        //authentication verification
        //request URI
        String requestUri = request.getRequestURI();
        Object principal = authentication.getPrincipal();

        if (principal == null || "anonymousUser".equals(principal)) {
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Admin admin = adminService.findAdminByUserName(username);
        if (admin == null) {
            return false;
        }
        if (admin.getId() == 1) {
            //super admin
            return true;
        }
        Long id = admin.getId();
        List<Permission> permissionList = this.adminService.getPermissionByUserId(id);
        requestUri = StringUtils.split(requestUri, '?')[0];
        for (Permission permission : permissionList) {
            if (requestUri.equals(permission.getPath())) {
                return true;
            }
        }




        return false;
    }
}
