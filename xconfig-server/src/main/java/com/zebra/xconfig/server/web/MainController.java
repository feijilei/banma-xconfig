package com.zebra.xconfig.server.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.server.po.KvPo;
import com.zebra.xconfig.server.po.ProfilePo;
import com.zebra.xconfig.server.po.ProjectPo;
import com.zebra.xconfig.server.service.WisdomService;
import com.zebra.xconfig.server.service.XKvService;
import com.zebra.xconfig.server.service.XProjectProfileService;
import com.zebra.xconfig.server.util.WebAttributeConstants;
import com.zebra.xconfig.server.util.zk.XConfigServer;
import com.zebra.xconfig.server.vo.AjaxResponse;
import com.zebra.xconfig.server.vo.KvVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ying on 16/7/19.
 */
@Controller
@RequestMapping("/main")
public class MainController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private XProjectProfileService xProjectProfileService;
    @Resource
    private XKvService xKvService;
    @Resource
    private WisdomService wisdomService;
    @Resource
    private XConfigServer xConfigServer;

    /**
     * 此首页已废弃，采用新的样式
     * @return
     */
    @Deprecated
    @RequestMapping("index2")
    public ModelAndView index2(){
        ModelAndView mv = new ModelAndView();

        List<String> projects = xProjectProfileService.queryAllProjects();

        mv.getModel().put("projects",projects);
        mv.getModel().put("projectsJson",JSON.toJSONString(projects));
        mv.getModel().put("wisdom",wisdomService.getOne());
        mv.setViewName("page/index2.ftl");
        return mv;
    }

    @RequestMapping("index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();

        List<ProjectPo> projectsPo = xProjectProfileService.queryAllProjectsPo();
        List<String> projectNames = new ArrayList<>();
        for(ProjectPo project : projectsPo){
            projectNames.add(project.getProject());
        }

        mv.getModel().put("projectsPo",projectsPo);
        mv.getModel().put("projectsJson",JSON.toJSONString(projectNames));
        mv.getModel().put("wisdom",wisdomService.getOne());
        mv.setViewName("page/index.ftl");
        return mv;
    }

    @RequestMapping("project")
    public ModelAndView project(WebRequest webRequest){
        ModelAndView mv = new ModelAndView();

        String project = HtmlUtils.htmlEscape(webRequest.getParameter("project"));
        String profile = HtmlUtils.htmlEscape(webRequest.getParameter("profile"));
        String allDep = webRequest.getParameter("allDep");
        int role = (int)webRequest.getAttribute("_role", RequestAttributes.SCOPE_REQUEST);

        List<String> profiles = xProjectProfileService.queryProjectProfiles(project);

        if(profiles != null){
            if(StringUtils.isBlank(profile)){
                profile = profiles.get(0);
            }
        }

        List<KvPo> kvPos;
        if(StringUtils.isNotBlank(allDep) && "true".equals(allDep)){
            kvPos = xKvService.queryByProjectAndProfileWithDeps(project,profile);
        }else{
            kvPos = xKvService.queryByProjectAndProfile(project,profile);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<KvVo> kvVos = new ArrayList<>();
        Map<String,KvVo> kvsMap = new HashMap<>();
        for(KvPo kvPo : kvPos){
            KvVo kvVo = new KvVo();
//            kvVo.setMkey(CommonUtil.genMKey(kvPo.getProject(), kvPo.getProfile(), kvPo.getxKey()));
            kvVo.setKey(CommonUtil.genKey(kvPo.getProject(),kvPo.getxKey()));

            //权限不够的高密字段隐藏
            if(!"N".equals(kvPo.getSecurity())){//高密字段
                if(kvPo.getProject().equals(project)){//当前项目
                    if(role < 20){//权限不够
                        kvVo.setValue("*********");
                    }else{
                        kvVo.setValue(kvPo.getxValue());
                    }
                }else{//非当前项目的高密字段
                    kvVo.setValue("*********");
                }
            }else {
                kvVo.setValue(kvPo.getxValue());
            }

            kvVo.setDescription(kvPo.getDescription());
            kvVo.setSecurity(kvPo.getSecurity());
            kvVo.setCreateTime(simpleDateFormat.format(kvPo.getCreateTime()));
            kvVo.setUpdateTime(simpleDateFormat.format(kvPo.getUpdateTime()));
            kvVo.setProject(kvPo.getProject());

            kvVos.add(kvVo);
            kvsMap.put(kvVo.getKey(),kvVo);
        }

        List<String> dependencies = this.xProjectProfileService.queryProjectDependencies(project);

        Set<String> clientIps = this.xConfigServer.getClientsIp(project,profile);

        //缺失key列表
        List<String> lostKeys = this.xKvService.filterLostKeys(project,profile);
        List<String> lostKeyStr = new ArrayList<>();
        for(String tmp : lostKeys){
            lostKeyStr.add(HtmlUtils.htmlEscape(tmp));
        }

        mv.getModel().put("project",project);
        mv.getModel().put("profile",profile);
        mv.getModel().put("profiles",profiles);
        mv.getModel().put("kvVos",kvVos);
        mv.getModel().put("kvmap", JSON.toJSONString(kvsMap));
        mv.getModel().put("allDep",allDep);
        mv.getModel().put("dependencies",dependencies);
        mv.getModel().put("clientIps",clientIps);
        mv.getModel().put("lostKeyStr",lostKeyStr);
        mv.setViewName("page/projectKv.ftl");

        return mv;
    }

    @RequestMapping("/addKvs")
    @ResponseBody
    public AjaxResponse addKvs(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();

        String project = HtmlUtils.htmlEscape(webRequest.getParameter("project"));
        String[] profiles = webRequest.getParameterValues("profiles");
        String key = HtmlUtils.htmlEscape(webRequest.getParameter("xkey"));
        String value = webRequest.getParameter("xvalue");
        try {
            String profile = HtmlUtils.htmlEscape(webRequest.getParameter("profile"));
            String security = HtmlUtils.htmlEscape(webRequest.getParameter("security"));
            String description = HtmlUtils.htmlEscape(webRequest.getParameter("description"));

            KvPo kvPo = new KvPo();
            kvPo.setProject(project);
            kvPo.setProfile(profile);
            kvPo.setxKey(key);
            kvPo.setxValue(value);
            kvPo.setSecurity(security);
            kvPo.setDescription(description);

            List<KvPo> kvPos = new ArrayList<>();
            kvPos.add(kvPo);

            if(profiles != null) {
                for (String pf : profiles) {
                    KvPo temp = new KvPo();
                    temp.setProject(project);
                    temp.setProfile(pf);
                    temp.setxKey(key);
                    temp.setxValue(value);
                    temp.setSecurity(security);
                    temp.setDescription(description);

                    kvPos.add(temp);
                }
            }

            this.xKvService.addKvs(kvPos);

            ajaxResponse.setMsg("增加成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            jsonObject.put("profiles",profiles);
            jsonObject.put("key",key);
            Cat.logEvent(project,"addkvs",ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(),jsonObject.toJSONString());
        }

        return ajaxResponse;
    }

    @RequestMapping("editKv")
    @ResponseBody
    public AjaxResponse editKv(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        String project = webRequest.getParameter("project");
        String profile = webRequest.getParameter("profile");
        String key = webRequest.getParameter("key");
        String value = webRequest.getParameter("xvalue");
        try{
            String description = HtmlUtils.htmlEscape(webRequest.getParameter("description"));

            String xkey = CommonUtil.genXKeyByKey(key);

            KvPo kvPo = new KvPo();
            kvPo.setProject(project);
            kvPo.setProfile(profile);
            kvPo.setxKey(xkey);
            kvPo.setxValue(value);
            kvPo.setDescription(description);

            this.xKvService.updateKv(kvPo);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            jsonObject.put("profile",profile);
            jsonObject.put("key",key);
            Cat.logEvent(project,"editKv",ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(),jsonObject.toJSONString());
        }

        return ajaxResponse;
    }

    @RequestMapping("removeKv")
    @ResponseBody
    public AjaxResponse removeKv(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();

        String key = webRequest.getParameter("key");
        String profile = webRequest.getParameter("profile");
        String project = webRequest.getParameter("project");
        try{

            this.xKvService.removeKvBykey(project,profile,key);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            jsonObject.put("profile",profile);
            jsonObject.put("key", key);
            Cat.logEvent(CommonUtil.genProjectByMkey(key),"editKv",ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(),jsonObject.toJSONString());
        }

        return ajaxResponse;
    }

    @RequestMapping("projectList")
    @ResponseBody
    public List<Map<String,Object>> projectList(WebRequest webRequest){

        String queryStr = webRequest.getParameter("q");

        if(StringUtils.isBlank(queryStr)){
            return null;
        }

        List<Map<String,Object>> ret = new ArrayList<>();

        List<String> projects = this.xProjectProfileService.queryProjectsByPrefix(queryStr);

        for(String project : projects){
            Map<String,Object> map = new HashMap<>();
            map.put("value",project);

            ret.add(map);
        }

        return ret;
    }

    @RequestMapping("updateProjectDeps")
    @ResponseBody
    public AjaxResponse updateProjectDeps(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();

        String project = HtmlUtils.htmlEscape(webRequest.getParameter("project"));
        String deps = HtmlUtils.htmlEscape(webRequest.getParameter("deps"));
        try {

            Set<String> pDeps = new HashSet<>();
            String[] depArray = deps.split(",");
            for(int i = 0 ; i < depArray.length ; i++){
                pDeps.add(depArray[i].trim());
            }

            this.xProjectProfileService.addDepenedencies(project, pDeps);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            jsonObject.put("deps",deps);
            Cat.logEvent(project,"updateProjectDeps",ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(),jsonObject.toJSONString());
        }

        return ajaxResponse;
    }

    @RequestMapping("addProfile")
    @ResponseBody
    public AjaxResponse addProfile(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        String project = HtmlUtils.htmlEscape(webRequest.getParameter("project"));
        String profile = HtmlUtils.htmlEscape(webRequest.getParameter("addProfileName"));
        String source = HtmlUtils.htmlEscape(webRequest.getParameter("cpSource"));
        try {
            String profileKey = HtmlUtils.htmlEscape(webRequest.getParameter("profileKey"));

            ProfilePo profilePo = new ProfilePo();
            profilePo.setProject(project);
            profilePo.setProfile(profile);
            profilePo.setProfileKey(profileKey);

            this.xProjectProfileService.addProfile(profilePo,source);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            jsonObject.put("project",project);
            jsonObject.put("profile",profile);
            jsonObject.put("source",source);
            Cat.logEvent(project,"addProfile",ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(),jsonObject.toJSONString());
        }
        return ajaxResponse;
    }


    @RequestMapping("removeProfile")
    @ResponseBody
    public AjaxResponse removeProfile(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        String project = HtmlUtils.htmlEscape(webRequest.getParameter("project"));
        String profile = HtmlUtils.htmlEscape(webRequest.getParameter("removeProfile"));
        try {

            this.xProjectProfileService.removeProfile(project, profile);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            jsonObject.put("profile",profile);
            Cat.logEvent(project,"removeProfile",ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(),jsonObject.toJSONString());
        }
        return ajaxResponse;
    }

    @RequestMapping("addProject")
    @ResponseBody
    public AjaxResponse addProject(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        String project = HtmlUtils.htmlEscape(webRequest.getParameter("addProjectName"));
        String profileStr = HtmlUtils.htmlEscape(webRequest.getParameter("preProfiles"));
        String description = HtmlUtils.htmlEscape(webRequest.getParameter("description"));
        try{

            String[] profiles = StringUtils.isBlank(profileStr) ? null : profileStr.split(",");
            this.xProjectProfileService.addProject(project,description,profiles);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            jsonObject.put("profile",profileStr);
            Cat.logEvent(project,"addProject",ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(),jsonObject.toJSONString());
        }
        return ajaxResponse;
    }

    @RequestMapping("removeProject")
    @ResponseBody
    public AjaxResponse removeProject(HttpServletRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        String project = HtmlUtils.htmlEscape(webRequest.getParameter("project"));
        try{

            this.xProjectProfileService.removeProject(project);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("operator",webRequest.getAttribute(WebAttributeConstants.USER_NIKE));
            Cat.logEvent(project,"removeProject",ajaxResponse.getCode() == 0 ? Message.SUCCESS : ajaxResponse.getMsg(),jsonObject.toJSONString());
        }
        return ajaxResponse;
    }
}
