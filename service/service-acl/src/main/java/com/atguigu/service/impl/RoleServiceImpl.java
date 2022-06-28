package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass =RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired  //Spring框架提供的依赖注入注解   先byType再byName
    //@Resource //JDK提供的依赖注入注解   先byName再byType
    RoleDao roleDao;

    @Autowired
    AdminRoleDao adminRoleDao;

    @Override
    public BaseDao<Role> getEntityDao() {
        return roleDao;
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Map getSelectByAdminId(Long id) {
        Map map = new HashMap();
        List noAssignRoleList = new ArrayList(); //存放未分配角色
        List assignRoleList = new ArrayList(); //存放已分配角色

        //1.查询所有角色
        List<Role> allList = roleDao.findAll();

        //2.查询该用户所拥有的角色id集合
        List<Long> roleIdList = adminRoleDao.findRoleIdListByAdminId(id);

        //3.将所有角色划分到两个集合中返回
        for (Role role : allList) {
            Long roleId = role.getId();
            if(roleIdList.contains(roleId)){ //迭代角色id如果在已拥有的角色集合中存在，说明这个角色已分配角色
                assignRoleList.add(role);
            }else{
                noAssignRoleList.add(role);
            }
        }

        map.put("noAssignRoleList",noAssignRoleList);
        map.put("assignRoleList",assignRoleList);
        return map;
    }

    @Override
    public void assignRole(Long adminId, Long[] roleIds) {
        //1.先删除已有角色
        adminRoleDao.deleteAdminRoleRelationship(adminId);
        //2.重新分配新的角色
        if(roleIds!=null && roleIds.length >0){
            List list = new ArrayList();
            for (Long roleId : roleIds) {
                if(StringUtils.isEmpty(roleId)){ // "1,2,3,4," 通过逗号分解后，数组中可能出现空值，需要逻辑判断。
                    continue;
                }
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(roleId);
                list.add(adminRole);
            }
            adminRoleDao.insertBatch(list);
        }
    }
}
