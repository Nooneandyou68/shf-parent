package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;

import java.util.List;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/27 19:50
 */
public interface RolePermissionDao extends BaseDao<RolePermission> {
    void deleteByRoleId(Long roleId);
    List<Long> findPermissionIdListByRoleId(Long roleId);
}
