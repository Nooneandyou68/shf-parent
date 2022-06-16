package com.atguigu.dao;

import com.atguigu.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao {
    List<Role> findAll();
    Integer insert(Role role);
}