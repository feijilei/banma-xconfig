package com.zebra.xconfig.server.web;

import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.zebra.xconfig.server.service.XProjectProfileService;
import com.zebra.xconfig.server.service.XUserService;
import com.zebra.xconfig.server.util.WebAttributeConstants;
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
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
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

        List<String> dProjects = this.xProjectProfileService.queryProjectsByDepedProject(project);

        mv.getModel().put("dProjects",dProjects);
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
    public AjaxResponse addOwner(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        String userName = webRequest.getParameter("addOwer");
        String project = webRequest.getParameter("project");
        try{

            this.xUserService.addUserProjectRole(project,userName);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            jsonObject.put("owner",userName);
            Cat.logEvent(project, "addOwner", ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(), jsonObject.toJSONString());
        }
        return ajaxResponse;
    }

    @RequestMapping("removeOwner")
    @ResponseBody
    public AjaxResponse removeOwner(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        String userName = webRequest.getParameter("userName");
        String project = webRequest.getParameter("project");
        try{

            this.xUserService.removeUserProjectRole(project,userName);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            jsonObject.put("owner",userName);
            Cat.logEvent(project,"removeOwner",ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(),jsonObject.toJSONString());
        }
        return ajaxResponse;
    }

    @RequestMapping("profilesOrder")
    public ModelAndView profilesOrder(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        List<String> profiles = this.xProjectProfileService.queryProfilesOrder();

        mv.getModel().put("profiles",profiles);
        mv.setViewName("page/profileOrder.ftl");
        return mv;
    }

    @RequestMapping("saveProfilesOrder")
    @ResponseBody
    public AjaxResponse saveProfilesOrder(HttpServletRequest request){
        AjaxResponse ajaxResponse = new AjaxResponse();
        try {
            String profiles = request.getParameter("profiles");

            List<String> profileList = new ArrayList<>();
            String[] arr = profiles.split(",");
            for(int i = 0 ; i < arr.length ; i++ ){
                profileList.add(i,arr[i]);
            }
            this.xProjectProfileService.saveProfilesOrder(profileList);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }
        return ajaxResponse;
    }
}
