package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/22 19:50
 */
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String ACTION_LIST = "redirect:/house";

    private static final String PAGE_DETAIL = "house/detail";

    @Reference
    HouseService houseService;

    @Reference
    CommunityService communityService;

    @Reference
    DictService dictService;

    @Reference
    HouseImageService houseImageService;

    @Reference
    HouseBrokerService houseBrokerService;

    @Reference
    HouseUserService houseUserService;



    @RequestMapping
    public String index(Map map, HttpServletRequest request) {
        Map<String, Object> filters = getFilters(request);
        //1.分页数据
        PageInfo<House> page = houseService.findPage(filters);
        //2.所有小区数据
        List<Community> communityList = communityService.findAll();
        //3.6个数据字段集合
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");
        //4.filters数据，用于回显

        map.put("page",page);
        map.put("communityList",communityList);

        map.put("houseTypeList",houseTypeList);
        map.put("floorList",floorList);
        map.put("buildStructureList",buildStructureList);
        map.put("directionList",directionList);
        map.put("decorationList",decorationList);
        map.put("houseUseList",houseUseList);

        map.put("filters",filters);
        return PAGE_INDEX;
    }

    @RequestMapping("/create")
    public String create(Map map){
        //2.所有小区数据
        List<Community> communityList = communityService.findAll();
        //3.6个数据字段集合
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");

        map.put("communityList",communityList);

        map.put("houseTypeList",houseTypeList);
        map.put("floorList",floorList);
        map.put("buildStructureList",buildStructureList);
        map.put("directionList",directionList);
        map.put("decorationList",decorationList);
        map.put("houseUseList",houseUseList);
        return PAGE_CREATE;
    }

    @RequestMapping("/save")
    public String save(House house, HttpServletRequest request) {
        houseService.insert(house);
        return this.successPage(null, request);
    }

    @RequestMapping("/edit/{id}")
    public String edit(Map map, @PathVariable("id") Long id) {
        //1、查询房子实体对象
        House house = houseService.getById(id);
        //2.所有小区数据
        List<Community> communityList = communityService.findAll();
        //3.6个数据字段集合
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");

        map.put("house", house);
        map.put("communityList", communityList);

        map.put("houseTypeList", houseTypeList);
        map.put("floorList", floorList);
        map.put("buildStructureList", buildStructureList);
        map.put("directionList", directionList);
        map.put("decorationList", decorationList);
        map.put("houseUseList", houseUseList);
        return PAGE_EDIT;
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        houseService.delete(id);
        return ACTION_LIST;
    }

    @RequestMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        houseService.publish(id, status);
        return ACTION_LIST;
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Map map) {
        //1、房源对象
        House house = houseService.getById(id);//在业务层重写了getById方法
        //2、小区对象
        Community community = communityService.getById(house.getCommunityId());
        //3、房源图片集合
        List<HouseImage> houseImage1List = houseImageService.findList(id, 1);
        List<HouseImage> houseImage2List = houseImageService.findList(id, 2);
        //4、经纪人集合
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);
        //5、房东集合
        List<HouseUser> houseUserList = houseUserService.findListByHouseId(id);
        map.put("house", house);
        map.put("community", community);
        map.put("houseImage1List", houseImage1List);
        map.put("houseImage2List", houseImage2List);
        map.put("houseBrokerList", houseBrokerList);
        map.put("houseUserList", houseUserList);
        return PAGE_DETAIL;
    }
}
