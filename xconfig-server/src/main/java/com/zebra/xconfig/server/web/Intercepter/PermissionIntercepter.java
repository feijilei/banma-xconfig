package com.zebra.xconfig.server.web.Intercepter;

import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.server.dao.mapper.XUserMapper;
import com.zebra.xconfig.server.po.UserPo;
import com.zebra.xconfig.server.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ying on 16/8/1.
 */
public class PermissionIntercepter extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String,Integer> resources = new HashMap<>();

    @Autowired
    private XUserMapper xUserMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String username = null;
            String security = null;
            String project = request.getParameter("project");

            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if ("un".equals(cookie.getName())) {
                    username = cookie.getValue();
                } else if ("s".equals(cookie.getName())) {
                    security = cookie.getValue();
                } else {

                }
            }

            if (StringUtils.isBlank(username) || StringUtils.isBlank(security)) {
                throw  new XConfigException("cookie获取不到签名信息，重定向到登录界面");
            }

            //校验签名信息
            UserPo userPo = xUserMapper.loadUser(username);
            if(userPo == null){
                throw new XConfigException("当前用户不存在");
            }
            String genSecurity = UserUtil.genSecurityKey(username,userPo.getPassword(),userPo.getSalt());
            if(!genSecurity.equals(security)){
                throw new XConfigException("cookie验证失败，需要重新登录");
            }

            //校验权限信息
            int role = 0;
            if(StringUtils.isBlank(project)){
                role = userPo.getRole();
            }else{
                Integer projectRole = xUserMapper.loadUserProjectRole(username,project);
                role = (projectRole != null && projectRole >= role) ? projectRole : role;
            }
            String url = request.getRequestURI();
            int needRole = this.resources.get(url) != null ? this.resources.get(url) : 999999999;//防止忘记设置权限带来的安全问题
            if(role >= needRole){
                request.setAttribute("_role",role);
                return true;
            }else{
                throw new XConfigException("您的权限不足");
            }

        }catch (XConfigException e){
            logger.error(e.getMessage(), e);
            request.getRequestDispatcher("/index").forward(request,response);
            return  false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String _role = request.getParameter("_role");
        if(StringUtils.isNotBlank(_role)){
            modelAndView.getModel().put("_role",_role);
        }
    }

    public void setResources(Map<String, Integer> resources) {
        this.resources = resources;
    }
}
