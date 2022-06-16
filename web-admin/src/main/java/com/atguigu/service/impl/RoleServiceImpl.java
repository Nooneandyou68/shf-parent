package com.atguigu.service.impl;

import com.atguigu.dao.RoleDao;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

   @Autowired
   private RoleDao roleDao;

   public List<Role> findAll() {
      return roleDao.findAll();
   }

   @Override
   public Integer insert(Role role) {
      return roleDao.insert(role);
   }
}