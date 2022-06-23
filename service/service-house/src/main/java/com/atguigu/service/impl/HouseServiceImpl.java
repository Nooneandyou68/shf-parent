package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.service.DictService;
import com.atguigu.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/22 19:52
 */
@Service(interfaceClass = HouseService.class)
@Transactional
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {

    @Autowired
    HouseDao houseDao;

    @Autowired
    DictDao dicDao;

    @Override
    public BaseDao<House> getEntityDao() {
        return houseDao;
    }

    @Override
    public void publish(Long id, Integer status) {
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseDao.update(house);
    }

    @Override
    public House getById(Serializable id) {
        House house = houseDao.getById(id);
        if (house != null) {
            if(null == house) return null;
            //户型：
            String houseTypeName = dicDao.getNameById(house.getHouseTypeId());
            //楼层
            String floorName = dicDao.getNameById(house.getFloorId());
            //建筑结构：
            String buildStructureName = dicDao.getNameById(house.getBuildStructureId());
            //朝向：
            String directionName = dicDao.getNameById(house.getDirectionId());
            //装修情况：
            String decorationName = dicDao.getNameById(house.getDecorationId());
            //房屋用途：
            String houseUseName = dicDao.getNameById(house.getHouseUseId());
            house.setHouseTypeName(houseTypeName);
            house.setFloorName(floorName);
            house.setBuildStructureName(buildStructureName);
            house.setDirectionName(directionName);
            house.setDecorationName(decorationName);
            house.setHouseUseName(houseUseName);
        }
        return super.getById(id);
    }

}
