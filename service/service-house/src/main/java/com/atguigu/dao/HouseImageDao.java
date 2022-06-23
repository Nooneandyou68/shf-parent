package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/23 15:30
 */
public interface HouseImageDao extends BaseDao<HouseImage> {
    List<HouseImage> findList(@Param("houseId") Long houseId, @Param("type") Integer type);
}
