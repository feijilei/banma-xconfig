package com.zebra.xconfig.server.web;

import com.zebra.xconfig.server.service.XProjectProfileService;
import com.zebra.xconfig.server.service.XUserService;
import com.zebra.xconfig.server.vo.AjaxResponse;
import com.zebra.xconfig.server.vo.XUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ying on 16/7/29.
 */
@Controller
@RequestMapping("/project")
public class ProjectController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private XProjectProfileService xProjectProfileService;
    @Resource
    private XUserService xUserService;

    @RequestMapping("setting")
    public ModelAndView setting(WebRequest webRequest){
        ModelAndView mv = new ModelAndView();

        String project = webRequest.getParameter("project");

        List<XUserVo> xUserVos = this.xUserService.queryProjectOwner(project);

        mv.getModel().put("project",project);
        mv.getModel().put("xUserVos",xUserVos);
        mv.setViewName("page/projectSetting.ftl");
        return mv;
    }

    @RequestMapping("queryGuestUser")
    @ResponseBody
    public List<String> queryGuestUser(WebRequest webRequest){
        String userNamePre = webRequest.getParameter("term");
        try {
            List<String> guests = this.xUserService.queryGuestUser(userNamePre);
            return guests;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    @RequestMapping("addOwner")
    @ResponseBody
    public AjaxResponse addOwner(WebRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        try{
            String userName = webRequest.getParameter("addOwer");
            String project = webRequest.getParameter("project");

            this.xUserService.addUserProjectRole(project,userName);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }
        return ajaxResponse;
    }

    @RequestMapping("removeOwner")
    @ResponseBody
    public AjaxResponse removeOwner(WebRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        try{
            String userName = webRequest.getParameter("userName");
            String project = webRequest.getParameter("project");

            this.xUserService.removeUserProjectRole(project,userName);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }
        return ajaxResponse;
    }
}
