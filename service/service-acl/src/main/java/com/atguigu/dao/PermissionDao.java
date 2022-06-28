package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Permission;

import java.util.List;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/27 19:31
 */
public interface PermissionDao extends BaseDao<Permission> {

    List<Permission> findAll();

    List<Permission> findPermissionListByAdminId(Long adminId);

    List<String> findCodeByAdminId(Long adminId);

    List<String> findAllCode();
}
