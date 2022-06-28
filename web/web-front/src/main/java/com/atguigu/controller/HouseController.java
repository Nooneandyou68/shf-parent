package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController  //相当于组合了这两个注解： @Controller+@ResponseBody ，这样就不需要在每一个方法上增加@ResponseBody
@RequestMapping("/house")
public class HouseController {

    @Reference
    HouseService houseService;
    
    @Reference
    CommunityService communityService;
    
    @Reference
    HouseBrokerService houseBrokerService;
    
    @Reference
    HouseImageService houseImageService;

    @Reference
    UserFollowService userFollowService;

    @RequestMapping("/info/{id}")
    public Result<Map<String,Object>> list(@PathVariable("id") Long id, HttpServletRequest request){

        House house = houseService.getById(id);
        Community community = communityService.getById(house.getCommunityId());

        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);

        List<HouseImage> houseImage1List = houseImageService.findList(id, 1); //获取房源图片

        //--------补充代码 2022-06-25 11:30 --------------
        //登录后，判断当前用户是否已经关注房源，将isFollow值变成动态
        boolean isFollow = false;
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        if(userInfo!=null){
            Long userInfoId = userInfo.getId();
            isFollow = userFollowService.isFollow(userInfoId,id);
        }
        //-----------------------------------------------
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("house",house);
        map.put("community",community);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseImage1List",houseImage1List);
        map.put("isFollow",isFollow);
        return Result.ok(map);
    }

    //异步请求处理
    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result<PageInfo<HouseVo>> list(@PathVariable("pageNum") Integer pageNum,
                                @PathVariable("pageSize") Integer pageSize,
                                @RequestBody HouseQueryVo houseQueryVo){ // 坑 通过@RequestBody注解获取请求体json，转换为vo对象
        PageInfo<HouseVo> page = houseService.findListPage(pageNum, pageSize, houseQueryVo);
        return Result.ok(page); //将分页数据封装在Result中返回。转换为JSON串返回。
    }
}
