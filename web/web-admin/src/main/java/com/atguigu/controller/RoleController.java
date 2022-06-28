package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static final String PAGE_INDEX = "role/index";
    private static final String PAGE_CREATE = "role/create";
    private static final String ACTION_LIST = "redirect:/role";
    private static final String PAGE_EDIT = "role/edit";
    private static final String PAGE_ASSIGN_SHOW = "role/assignShow";

    @Reference
    RoleService roleService;

    @Reference
    PermissionService permissionService;

    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId") Long roleId,
                             @RequestParam("permissionIds")  Long[] permissionIds,  //springmvc框架将字符串自动转换为数组 [1,2,4,5,6]
                             HttpServletRequest request){
        //注意：操作中间表数据，通过多对多两端任意服务接口都行。中间表就不提供业务接口。
        permissionService.assignPermission(roleId,permissionIds);
        return this.successPage(null,request);
    }

    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignPermission/{roleId}")
    public String assignPermission(@PathVariable("roleId") Long id,Map map){
        //{ id:2, pId:0, name:"随意勾选 2", checked:true, open:true}
        List<Map<String,Object>> permissionList = permissionService.findPermissionByroleId(id);
        map.put("zNodes", JSON.toJSONString(permissionList));
        //map.put("zNodes",permissionList);
        map.put("roleId",id);
        return PAGE_ASSIGN_SHOW;
    }

    @RequestMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('role.delete')")
    public String delete(@PathVariable("id") Long id){
        Integer count = roleService.delete(id); //返回结果表示sql语句对数据库起作用的行数
        return ACTION_LIST;
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('role.edit')")
    public String update(Role role,Map map,HttpServletRequest request){ //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        roleService.update(role);
        return this.successPage("修改成功,哈哈",request);
    }

    @RequestMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('role.edit')")
    public String edit(@PathVariable("id") Long id, Map map){
        Role role = roleService.getById(id);
        map.put("role",role);
        return PAGE_EDIT;
    }

    @RequestMapping("/save")
    @PreAuthorize("hasAuthority('role.create')")
    public String save(Role role,Map map,HttpServletRequest request){ //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        roleService.insert(role);
        return this.successPage("添加成功,哈哈",request);
    }

   /* @RequestMapping("/save")
    public String save(Role role,Map map){ //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        roleService.insert(role);
        //return ACTION_LIST;
        map.put("messagePage","添加成功,哈哈");
        return PAGE_SUCCESS;
    }*/

    @RequestMapping("/create")
    @PreAuthorize("hasAuthority('role.create')")
    public String create(){
        return PAGE_CREATE;
    }

    /*@RequestMapping
    public String index(Map map){
        List<Role> list = roleService.findAll();

        map.put("list",list);

        return PAGE_INDEX;
    }*/

    /**
     * 分页查询
     *      根据查询条件进行查询
     *          roleName = ''
     *          pageNum = 1   隐藏域
     *          pageSize = 10  隐藏域
     * @param map
     * @return
     */
    @RequestMapping
    @PreAuthorize("hasAuthority('role.show')") //设置权限控制注解，在访问方法前需要校验用户权限
    public String index(HttpServletRequest request,Map map){
        Map<String,Object> filters =  getFilters(request);
        //分页对象，将集合数据，pageNum,pageSize,pages,total等
        PageInfo<Role> pageInfo = roleService.findPage(filters);
        map.put("page",pageInfo);
        map.put("filters",filters);
        return PAGE_INDEX;
    }

}
