package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {
    List<Map<String, Object>> findZnodesByParentId(Long parentId);

    List<Dict> findListByParentId(Long parentId);

    List<Dict> findListByDictCode(String dictCode);

    String getNameById(Long id);
}