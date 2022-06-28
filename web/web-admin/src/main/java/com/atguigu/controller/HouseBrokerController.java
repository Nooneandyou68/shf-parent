package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.base.BaseService;
import com.atguigu.entity.Admin;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {

    private static final String PAGE_CREATE = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";
    private static final String ACTION_LIST = "redirect:/house/detail/";
    @Reference
    HouseBrokerService houseBrokerService;

    @Reference
    AdminService adminService;


    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id){
        houseBrokerService.delete(id);
        return ACTION_LIST+houseId;
    }

    @RequestMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,HouseBroker houseBroker,HttpServletRequest request){
        //修改经纪人，名称和头像也需要更新
        Long adminId = houseBroker.getBrokerId();
        Admin admin = adminService.getById(adminId);
        HouseBroker dbBroker = houseBrokerService.getById(id);
        //dbBroker.setBrokerId(houseBroker.getBrokerId()); //将接收表单的数据一个一个拷贝到当前对象中，比较麻烦。可以采用工具方法批量拷贝。
        BeanUtils.copyProperties(houseBroker,dbBroker); //将前一个bean对象的属性拷贝到后一个对象中，只要属性名称一致。
        dbBroker.setBrokerName(admin.getName());
        dbBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.update(dbBroker);
        return this.successPage(null,request);
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Map map){
        HouseBroker houseBroker = houseBrokerService.getById(id);
        List<Admin> adminList = adminService.findAll();
        map.put("adminList",adminList);
        map.put("houseBroker",houseBroker);
        return PAGE_EDIT;
    }

    @RequestMapping("/save")
    public String save(HouseBroker houseBroker, HttpServletRequest request){
        Long adminId = houseBroker.getBrokerId();
        Admin admin = adminService.getById(adminId);
        //给冗余字段赋值，值来自于Admin
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.insert(houseBroker);
        return this.successPage(null,request);
    }

    @RequestMapping("/create")
    //public String create(Long houseId){
    public String create(HouseBroker houseBroker, Map map){ //参数houseId封装在houseBroker对象中
        List<Admin> adminList = adminService.findAll();

        map.put("adminList",adminList);
        map.put("houseBroker",houseBroker);
        return PAGE_CREATE;
    }


}
