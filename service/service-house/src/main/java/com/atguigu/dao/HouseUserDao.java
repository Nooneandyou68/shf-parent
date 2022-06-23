package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.HouseUser;

import java.util.List;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/23 15:35
 */
public interface HouseUserDao extends BaseDao<HouseUser> {
    List<HouseUser> findListByHouseId(Long houseId);
}
