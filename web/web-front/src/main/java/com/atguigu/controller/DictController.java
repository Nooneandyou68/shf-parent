package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {


   @Reference
   DictService dictService;


   /**
    * 根据上级id获取子节点数据列表
    * @param parentId
    * @return
    */
   @GetMapping(value = "/findListByParentId/{parentId}")
   @ResponseBody
   public Result<List<Dict>> findListByParentId(@PathVariable Long parentId) {
      List<Dict> list = dictService.findListByParentId(parentId);
      return Result.ok(list);
   }

   /**
    * 根据编码获取子节点数据列表
    * @param dictCode
    * @return
    */
   @GetMapping(value = "/findListByDictCode/{dictCode}")
   @ResponseBody
   public Result<List<Dict>> findListByDictCode(@PathVariable String dictCode) {
      List<Dict> list = dictService.findListByDictCode(dictCode);
      return Result.ok(list);
   }
}
