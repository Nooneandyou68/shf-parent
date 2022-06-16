package com.atguigu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/15 15:31
 */
@Controller
@RequestMapping
public class IndexController {

    private final static String PAGE_INDEX = "frame/index";
    private final static String PAGE_MAIN = "frame/main";

    /**
     * 框架首页
     */
    @GetMapping("/")
    public String index() {
        return PAGE_INDEX;
    }

    /**
     * 框架主页
     */
    @GetMapping("/main")
    public String main() {
        return PAGE_MAIN;
    }
}
