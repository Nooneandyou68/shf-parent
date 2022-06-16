package com.atguigu.service;

import com.atguigu.entity.Role;
import java.util.List;

public interface RoleService {
    List<Role> findAll();
    Integer insert(Role role);
}