package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/23 18:30
 */
@Controller
@RequestMapping(value = "/houseUser")
public class HouseUserController extends BaseController {

    private static final String PAGE_CREATE = "houseUser/create";
    private final static String PAGE_EDIT = "houseUser/edit";
    private final static String LIST_ACTION = "redirect:/house/";


    @Reference
    HouseUserService houseUserService;

    @RequestMapping("/create")
    public String create(Map map, HouseUser houseUser) {
        map.put("houseUser", houseUser);
        return PAGE_CREATE;
    }

    @RequestMapping("/save")
    public String save(HouseUser houseUser, HttpServletRequest request) {
        houseUserService.insert(houseUser);
        return this.successPage(null, request);
    }

    @RequestMapping("edit/{id}")
    public String edit(@PathVariable("id") Long id, Map map) {
        HouseUser houseUser = houseUserService.getById(id);
        map.put("houseUser", houseUser);
        return PAGE_EDIT;
    }

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId, @PathVariable("id")Long id) {
        houseUserService.delete(id);
        return LIST_ACTION + houseId;
    }

    @RequestMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, HouseUser houseUser, HttpServletRequest request) {
        HouseUser userServiceById = houseUserService.getById(id);
        BeanUtils.copyProperties(houseUser,userServiceById);
        houseUserService.update(userServiceById);
        return this.successPage(null, request);
    }
}
