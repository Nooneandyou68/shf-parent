package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.House;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/22 19:51
 */
public interface HouseService extends BaseService<House> {
    void publish(Long id, Integer status);
}
