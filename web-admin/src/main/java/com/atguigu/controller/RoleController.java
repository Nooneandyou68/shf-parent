package com.atguigu.controller;

import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/role")
@SuppressWarnings({"unchecked", "rawtypes"})
public class RoleController {

   @Autowired
   private RoleService roleService;

   private final static String PAGE_INDEX = "role/index";
   private final static String PAGE_CREATE = "role/create";
   public static final String LIST_ACTION = "redirect:/role";
   private final static String PAGE_SUCCESS = "common/successPage";
   /** 
    * 列表
    * @param model
    * @return
    */
   @RequestMapping
   public String index(ModelMap model) {
      List<Role> list = roleService.findAll();
      model.addAttribute("list", list);
      return PAGE_INDEX;
   }
   /**
    *添加按钮
    *@author SongBoHao
    *@date 2022/6/15 16:49
    *@param
    *@return
    */
   @GetMapping("/create")
   public String create(ModelMap model) {
      return PAGE_CREATE;
   }

   @PostMapping("/save")
   public String save(Role role, Map map) {
      map.put("messagePage", "操作成功");
      roleService.insert(role);
      return PAGE_SUCCESS;
   }
}