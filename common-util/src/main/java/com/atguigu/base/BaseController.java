package com.atguigu.base;

import com.github.pagehelper.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * @PROJECT_NAME: shf-parent
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2022/6/17 19:54
 */
public class BaseController {
    private final static String PAGE_SUCCESS = "common/successPage";
    //提示信息
    public final static String MESSAGE_SUCCESS = "操作成功！！！";

    /**
     * 成功页
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/6/17 19:56
     */
    protected String successPage(String message, HttpServletRequest request) {
        request.setAttribute("messagePage", StringUtil.isNotEmpty(message));
        return PAGE_SUCCESS;
    }

    /**
     * 封装页面提交的分页参数及搜索条件
     *
     * @param
     * @return
     * @author SongBoHao
     * @date 2022/6/17 19:58
     */
    protected Map<String, Object> getFilters(HttpServletRequest request) {
        Enumeration parameterNames = request.getParameterNames();
        TreeMap<String, Object> filters = new TreeMap<>();
        while (parameterNames != null && parameterNames.hasMoreElements()) {
            String paramName = (String) parameterNames.nextElement();
            String[] values = request.getParameterValues(paramName);
            if (values != null && values.length != 0) {
                if (values.length > 1) {
                    filters.put(paramName, values);
                }else {
                    filters.put(paramName, values[0]);
                }
            }
        }
        //如果没有提交请求参数，给就这两个参数赋予默认值。
        if (!filters.containsKey("pageNum")) {
            filters.put("pageNum", 1);
        }
        if (!filters.containsKey("pageSize")) {
            filters.put("pageSize", 2);
        }

        return filters;
    }
}
