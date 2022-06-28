package com.atguigu.interceptor;

import com.alibaba.fastjson.JSON;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 访问controller前拦截，判断是否登录，如果登录放行，否则，拒绝访问controller
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfo userInfo =  (UserInfo) request.getSession().getAttribute("USER");
        if(userInfo == null){
            Result result = Result.build("未登录,请先登录!", ResultCodeEnum.LOGIN_AUTH);
            //xxxStr = JSON.toJSONString(result)
            //response.getWriter().print(xxxStr);
            WebUtil.writeJSON(response,result); //将数据转换为json返回给客户端浏览器
            return false; //拒绝访问，返回错误提示
        }
        return true; //放行
    }
}
