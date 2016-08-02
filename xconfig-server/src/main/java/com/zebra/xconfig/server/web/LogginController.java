package com.zebra.xconfig.server.web;

import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.server.service.XUserService;
import com.zebra.xconfig.server.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ying on 16/8/1.
 */
@Controller
public class LogginController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private XUserService xUserService;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        String _errMsg = (String)request.getAttribute("_errMsg");
        if(StringUtils.isNotBlank(_errMsg)){
            mv.getModel().put("errMsg",_errMsg);
        }

        mv.setViewName("page/login.ftl");
        return mv;
    }

    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request,HttpServletResponse response){
        ModelAndView mv = new ModelAndView();

        String errMsg = null;
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
                throw new XConfigException("用户名和密码不能为空");
            }

            UserVo userVo = this.xUserService.checkUserAndPassword(email,password);

            response.addCookie(new Cookie("un",userVo.getUserName()));
            Cookie s = new Cookie("s",userVo.getSecurity());
            s.setHttpOnly(true);
            response.addCookie(s);
            response.addCookie(new Cookie("nike",userVo.getUserNike()));
            response.addCookie(new Cookie("t", String.valueOf(userVo.getTimeMillis())));

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            errMsg = e.getMessage();

            mv.getModel().put("errMsg",errMsg);
            mv.setViewName("page/login.ftl");
            return mv;
        }

        mv.setViewName("redirect:/main/index");
        return mv;
    }

    @RequestMapping("logout")
    public ModelAndView logout(HttpServletRequest request,HttpServletResponse response){
        ModelAndView mv = new ModelAndView();

        mv.setViewName("page/login.ftl");
        return mv;
    }
}
