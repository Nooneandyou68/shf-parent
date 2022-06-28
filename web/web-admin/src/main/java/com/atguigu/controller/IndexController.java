package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class IndexController {

    private static final String PAGE_FRAME = "frame/index";
    private static final String PAGE_MAIN = "frame/main";
    private static final String PAGE_LOGIN = "frame/login";

    @Reference
    AdminService adminService;

    @Reference
    PermissionService permissionsService;

    @RequestMapping("/")
    public String index(Map map) {
        //准备两个数据

        //从session域中获取登录的用户信息，
        Long adminId = 1L;
        Admin admin = adminService.getById(adminId);
        List<Permission> permissionList = permissionsService.findMenuPermissionByAdminId(adminId);
        map.put("admin", admin);
        map.put("permissionList", permissionList);
        return PAGE_FRAME;
    }

    @RequestMapping("/main")
    public String main(){
        return PAGE_MAIN;
    }

    @RequestMapping("/login")
    public String login() {
        return PAGE_LOGIN;
    }
}
