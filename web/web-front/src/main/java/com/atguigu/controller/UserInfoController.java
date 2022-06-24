package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.vo.RegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    UserInfoService userInfoService;

    @RequestMapping("/getCode/{phone}")
    public Result getCode(@PathVariable("phone") String phone, HttpServletRequest request){
        //模拟发送手机验证码，后续电商系统正式使用
        String code = "1111";
        request.getSession().setAttribute("CODE",code);

        //往手机上发送校验码，模拟操作。

        return Result.ok(code);
    }

    //注意：ajax提交 post 请求 ，通过请求体获取json数据，转换为vo对象。
    @RequestMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo,HttpServletRequest request){
        String code = registerVo.getCode();
        String nickName = registerVo.getNickName();
        String password = registerVo.getPassword();
        String phone = registerVo.getPhone();
        //1.数据校验（数据合法性）
        if(StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(nickName) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(phone)){
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }

        //2.手机验证码是否有效（校验码5分钟过期）
        //3.手机验证码是否一致
        String sendCode = (String)request.getSession().getAttribute("CODE"); //以后可以从redis中获取。
        if(sendCode==null || !code.equals(sendCode)){
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }

        //4.手机号码是否被占用(表中字段unique)
        UserInfo userInfo = userInfoService.getByPhone(phone);
        if(userInfo!=null){
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }

        //5.封装数据，保存数据（密码加密存储）
        userInfo = new UserInfo();
        BeanUtils.copyProperties(registerVo,userInfo);  //类型可以不一样，属性属性名称一样，就可以拷贝
        userInfo.setPassword(MD5.encrypt(password));
        userInfo.setStatus(1);
        userInfoService.insert(userInfo);
        return Result.ok();
    }
}
