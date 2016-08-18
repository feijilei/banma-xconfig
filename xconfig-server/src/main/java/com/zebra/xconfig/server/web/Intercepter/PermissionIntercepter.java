package com.zebra.xconfig.server.web.Intercepter;

import com.alibaba.fastjson.JSON;
import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.common.exception.XConfigUserCheckException;
import com.zebra.xconfig.server.dao.mapper.XUserMapper;
import com.zebra.xconfig.server.po.UserPo;
import com.zebra.xconfig.server.util.UserUtil;
import com.zebra.xconfig.server.vo.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by ying on 16/8/1.
 */
public class PermissionIntercepter extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int expirySecond = 1000*60*60*24;//一天超时时间
    @Autowired
    private XUserMapper xUserMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        try {
            String username = null;
            String security = null;
            String project = null;
            if(url.startsWith("/main") || url.startsWith("/project")){//这两个路径下的请求我们附加上project角色
                project = request.getParameter("project");
            }
            long timeMillis = 0;

            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    if ("un".equals(cookie.getName())) {
                        username = cookie.getValue();
                    } else if ("s".equals(cookie.getName())) {
                        security = cookie.getValue();
                    }else if("t".equals(cookie.getName())){
                        timeMillis = cookie.getValue() == null ? 0 : Long.valueOf(cookie.getValue());
                    } else {

                    }
                }
            }

            if (StringUtils.isBlank(username) || StringUtils.isBlank(security) || timeMillis == 0) {
                throw  new XConfigException("请登录");
            }

            //登录是否过期
            if((System.currentTimeMillis() - timeMillis) > expirySecond){
                throw new XConfigUserCheckException("登录已过期，请重新登录");
            }

            //校验签名信息
            UserPo userPo = xUserMapper.loadUser(username);
            if(userPo == null){
                throw new XConfigUserCheckException("当前用户不存在");
            }
            String genSecurity = UserUtil.genSecurityKey(username,userPo.getPassword(),timeMillis,userPo.getSalt());
            if(!genSecurity.equals(security)){
                throw new XConfigUserCheckException("验证失败，需要重新登录");
            }

            //校验权限信息
            int role = userPo.getRole();
            if(StringUtils.isNotBlank(project)){
                Integer projectRole = xUserMapper.loadUserProjectRole(username,project);
                role = (projectRole != null && projectRole >= role) ? projectRole : role;
            }
            int needRole = UrlResouces.getResouceRole(url);//防止忘记设置权限带来的安全问题
            if(role >= needRole){
                request.setAttribute("_role",role);
                request.setAttribute("_userNike", URLDecoder.decode(userPo.getUserNike(), "utf-8"));
                request.setAttribute("_userName", userPo.getUserName());
                return true;
            }else{
                throw new XConfigException("您的权限不足");
            }

        }catch (XConfigException e){
            logger.error(e.getMessage(), e);

            if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) { //如果是ajax请求响应头会有x-requested-with
                AjaxResponse ajaxResponse = new AjaxResponse();
                ajaxResponse.setThrowable(e);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(JSON.toJSONString(ajaxResponse));
                out.flush();
            }else{
                request.setAttribute("_errMsg",e.getMessage());
                request.getRequestDispatcher("/index").forward(request,response);
            }

            return  false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) { //如果是ajax请求响应头会有x-requested-with

        }else {
            //只能是非ajax请求才能执行model里面的put操作，否则会报错
            Integer _role = (Integer)request.getAttribute("_role");
            if(_role != null){
                modelAndView.getModel().put("role",_role);
            }
            String _userNike = (String)request.getAttribute("_userNike");
            if(StringUtils.isNotBlank(_userNike)){
                modelAndView.getModel().put("userNike",_userNike);
            }
        }

        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","No-cache");
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control", "No-store");
    }
}
