package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.House;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;

public interface HouseDao extends BaseDao<House> {
    Page<HouseVo> findListPage(HouseQueryVo houseQueryVo);
}
