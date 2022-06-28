package com.atguigu.aop;


import com.atguigu.util.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ControllerAspect {

    Logger log = LoggerFactory.getLogger(ControllerAspect.class);

    //声明切入点表达式
    @Pointcut("execution(* com.atguigu.controller..*.*(..))")
    public void pointcut(){}

    //5种常用通知：前置通知、后置通知、异常通知、方法返回通知、环绕通知
    @Around(value = "pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable{
        //获取日志相关数据，通过slf4j打印日志（console）
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(!(authentication.getPrincipal() instanceof SecurityUser)) return pjp.proceed();
//        SecurityUser securityUser = (SecurityUser)authentication.getPrincipal();
        //后续登录实现了，补充
        Long userId = 1L;//securityUser.getId();
        String userName = "admin";//securityUser.getUsername();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String ip = IpUtil.getIpAddress(request);
        String classMethod = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();

        String param = "";
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            try {
                param += "param" + (i+1) + ":" + args[i] + ",";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long startTime = System.currentTimeMillis();
        try {
            Object response = pjp.proceed();
            return response;
        } catch (Exception e) {
            e.getStackTrace();
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            String logs = userId + "|" + userName + "|" + url + "|" + method + "|" + ip + "|" + classMethod + "|" + param + "|" + time;
            log.info("request log:" + logs);
        }
    }
}
