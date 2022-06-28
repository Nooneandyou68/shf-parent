package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.service.PermissionService;
import com.atguigu.util.PermissionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/27 19:29
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    PermissionDao permissionDao;

    @Autowired
    RolePermissionDao rolePermissionDao;

    @Override
    public BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    @Override
    public List<Map<String, Object>> findPermissionByroleId(Long id) {
        //1、查询所有许可（权限）节点
        List<Permission> permissionList = permissionDao.findAll();

        ArrayList<Map<String, Object>> zNodes = new ArrayList<>();
        //2、那些节点需要进行勾选，拆线呢但钱角色所对应的许可
        List<Long> permissionIdListByRoleId = rolePermissionDao.findPermissionIdListByRoleId(id);
        for (Permission permission : permissionList) {
            //无需设置open：true
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", permission.getId());
            map.put("pId", permission.getParentId());
            map.put("name", permission.getName());
            //checked表示复选框是否打勾，表示拥有的许可权限，进行回显
            if (permissionIdListByRoleId.contains(permission.getId())) {
                map.put("checked", true);
            }
            zNodes.add(map);
        }
        return zNodes;
    }

    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {
        //1、先删除旧的关系数据
        rolePermissionDao.deleteByRoleId(roleId);
        //2、再增加一批新的关系数据
        if (permissionIds != null && permissionIds.length > 0) {
            List<RolePermission> list = new ArrayList<>();
            for (Long permissionId : permissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setPermissionId(permissionId);
                rolePermission.setRoleId(roleId);
                list.add(rolePermission);
            }
            rolePermissionDao.insertBatch(list);
        }
    }

    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        //如果是系统（超级）管理员，默认id=1，直接查询所有权限，无需条件，关联查询
        List<Permission> list = null;
        if (adminId.intValue() == 1) {
             list = permissionDao.findAll();
        }else {
            //如果是普通管理员，就需要根据用户获取角色，在获取相应的权限，需要进行条件，关联查询
            list = permissionDao.findPermissionListByAdminId(adminId);
        }
        //把权限数据构建成树形结构数据
        List<Permission> result = PermissionHelper.bulid(list);
        return result;
    }

    @Override
    public List<Permission> findAllMenu() {
        //全部权限列表

        List<Permission> permissionList = permissionDao.findAll();
        if (CollectionUtils.isEmpty(permissionList)) {
            return null;
        }

        //构建数据树
        //把权限数据构建成树形机构数据
        List<Permission> result = PermissionHelper.bulid(permissionList);
        return result;
    }

    @Override
    public List<String> findCodeByAdminId(Long adminId) {
        return permissionDao.findCodeByAdminId(adminId);
    }

    @Override
    public List<String> findAllCode() {
        return permissionDao.findAllCode();
    }
}
