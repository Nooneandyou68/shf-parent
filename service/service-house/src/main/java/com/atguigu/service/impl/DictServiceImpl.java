package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;


import com.alibaba.fastjson.TypeReference;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = DictService.class)
@Transactional
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    DictDao dictDao;

    @Autowired
    JedisPool jedisPool;

    @Override
    public BaseDao<Dict> getEntityDao() {
        return dictDao;
    }

    //数据字典，树型结构，ztree
    @Override
    public List<Map<String, Object>> findZnodesByParentId(Long parentId) {
        //虽然可以返回泛型为map类型的集合，但是由于需要做业务处理，还是返回泛型为实体类型
        List<Dict> list = dictDao.findZnodesByParentId(parentId);

        //需要进行类型转换。
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        for (Dict dict : list) {
            // isParent:true 表示当前节点是否为父节点。有孩子就是父节点，没孩子就是子节点，也称为叶子节点。
            Map<String, Object> map = new HashMap<>(); //代表一个节点： { id:1, pId:0, name:"全部分类", isParent:true}
            map.put("id",dict.getId());
            map.put("pId",dict.getParentId());
            map.put("name",dict.getName());

            Long id = dict.getId();
            Long pId = id;
            int count = dictDao.countIsParent(pId); //根据pid统计孩子数量
            map.put("isParent",count>0?true:false);
            data.add(map);
        }

        return data;
    }

    @Override
    public List<Dict> findListByParentId(Long parentId) {
        Jedis jedis = null;
        try {
            String key = "shf:dict:parentId:"+parentId;
            //1.先从缓存中查询，如果有数据，直接返回，无需查询数据库
            jedis = jedisPool.getResource();
            String value = jedis.get(key); //存储时将List<Dict>转换为字符串存储的，获取得到的是字符串
            if(!StringUtils.isEmpty(value)){
                Type listType = new TypeReference<List<Dict>>(){}.getType();
                List<Dict> list = JSON.parseObject(value,listType);
                System.out.println("redis list = " + list);
                return list;
            }

            //2.如果缓存中没有，则查询数据库，并将数据存放到缓存中，给下次访问数据利用缓存。
            List<Dict> list2 = dictDao.findZnodesByParentId(parentId);
            //if(list2 != null && list2.size()>0){
            if(!CollectionUtils.isEmpty(list2)){
                jedis.set(key,JSON.toJSONString(list2));
                System.out.println("db list = " + list2);
                return list2;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public List<Dict> findListByDictCode(String dictCode) {
        Dict dict = dictDao.findDictByDictCode(dictCode);
        List<Dict> list = dictDao.findZnodesByParentId(dict.getId()); //拿主键当外键使用
        return list;
    }

    @Override
    public String getNameById(Long id) {
        return dictDao.getNameById(id);
    }
}
