package com.zebra.xconfig.server.web;

import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.server.service.XUserService;
import com.zebra.xconfig.server.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
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
    public ModelAndView index(WebRequest webRequest){
        ModelAndView mv = new ModelAndView();

        mv.setViewName("page/login.ftl");
        return mv;
    }

    @RequestMapping("/loggin")
    public ModelAndView loggin(HttpServletRequest request,HttpServletResponse response){
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
            response.addCookie(new Cookie("s",userVo.getSecurity()));

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
}
