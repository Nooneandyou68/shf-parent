package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/27 19:14
 */
public interface PermissionService extends BaseService<Permission> {

    List<Map<String, Object>> findPermissionByroleId(Long id);

    void assignPermission(Long roleId, Long[] permissionIds);

    List<Permission> findMenuPermissionByAdminId(Long adminId);

    /**
     * 菜单全部数据
     * @return
     */
    List<Permission> findAllMenu();

    List<String> findCodeByAdminId(Long id);

    List<String> findAllCode();
}
