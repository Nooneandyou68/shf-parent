package com.atguigu.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.Map;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/17 19:03
 */
public interface BaseDao<T> {
    Integer insert(T t);

    T getById(Serializable id);

    void update(T t);

    Integer delete(Serializable id);

    Page<T> findPage(Map<String, Object> filters);
}
