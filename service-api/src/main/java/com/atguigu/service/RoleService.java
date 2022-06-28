package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role> {  //ctrl + alt + b
    List<Role> findAll();

    Map getSelectByAdminId(Long id);

    //void assignRole(Long adminId, String roleIds);
    void assignRole(Long adminId, Long[] roleIds);

}
