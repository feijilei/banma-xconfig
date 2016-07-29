package com.zebra.xconfig.server.web;

import com.zebra.xconfig.server.service.XProjectProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by ying on 16/7/29.
 */
@Controller
@RequestMapping("/project")
public class ProjectController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private XProjectProfileService xProjectProfileService;

    @RequestMapping("setting")
    public ModelAndView setting(WebRequest webRequest){
        ModelAndView mv = new ModelAndView();

        String project = webRequest.getParameter("project");

        mv.getModel().put("project",project);
        mv.setViewName("page/projectSetting.ftl");
        return mv;
    }
}
