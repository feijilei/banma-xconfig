package com.zebra.xconfig.server.web;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一的错误处理页面
 */
public class ExceptionHandler implements HandlerExceptionResolver {
  
    @Override  
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();

        Cookie[] cookies = request.getCookies();
        for(int i = 0 ;i < cookies.length ; i++){
            if("debug".equals(cookies[i].getName())){
                StackTraceElement[] stackTraces = ex.getStackTrace();

                StringBuilder stackTraceStr = new StringBuilder();
                stackTraceStr.append(ex.toString())
                        .append("<br>");
                for(int j = 0 ; j < stackTraces.length ; j++){
                    stackTraceStr.append(stackTraces[j].toString())
                        .append("<br>");
                }

                mv.getModel().put("stackTraces",stackTraceStr);
            }
        }

        mv.setViewName("error.ftl");

        return mv;
    }  
  
}  