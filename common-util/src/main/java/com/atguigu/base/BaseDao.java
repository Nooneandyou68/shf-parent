package com.atguigu.base;

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

    Integer insert(T t);

    Integer insertBatch(List<T> list);

    T getById(Serializable id);

    void update(T t);

    Integer delete(Serializable id);

    Page<T> findPage(Map<String, Object> filters);
}
