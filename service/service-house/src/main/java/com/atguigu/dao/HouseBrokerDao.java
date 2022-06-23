package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.HouseBroker;

import java.util.List;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/23 15:26
 */
public interface HouseBrokerDao extends BaseDao<HouseBroker> {
    List<HouseBroker> findListByHouseId(Long houseId);
}
