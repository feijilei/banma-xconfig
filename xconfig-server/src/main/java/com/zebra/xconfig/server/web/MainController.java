package com.zebra.xconfig.server.web;

import com.alibaba.fastjson.JSON;
import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.server.po.KvPo;
import com.zebra.xconfig.server.service.XKvService;
import com.zebra.xconfig.server.service.XProjectProfileService;
import com.zebra.xconfig.server.vo.AjaxResponse;
import com.zebra.xconfig.server.vo.KvVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();

        List<String> projects = xProjectProfileService.queryAllProjects();

        mv.getModel().put("projects",projects);
        mv.setViewName("page/index.ftl");
        return mv;
    }

    @RequestMapping("project")
    public ModelAndView project(WebRequest webRequest){
        ModelAndView mv = new ModelAndView();

        String project = HtmlUtils.htmlEscape(webRequest.getParameter("project"));
        String profile = HtmlUtils.htmlEscape(webRequest.getParameter("profile"));


        List<String> profiles = xProjectProfileService.queryProjectProfiles(project);

        if(profiles != null){
            if(StringUtils.isBlank(profile)){
                profile = profiles.get(0);
            }
        }

        List<KvPo> kvPos = xKvService.queryByProjectAndProfile(project,profile);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<KvVo> kvVos = new ArrayList<>();
        Map<String,KvVo> kvsMap = new HashMap<>();
        for(KvPo kvPo : kvPos){
            KvVo kvVo = new KvVo();
            kvVo.setKey(CommonUtil.genMKey(project,profile,kvPo.getxKey()));
            kvVo.setValue(kvPo.getxValue());
            kvVo.setDescription(kvPo.getDescription());
            kvVo.setSecurity(kvPo.getSecurity());
            kvVo.setCreateTime(simpleDateFormat.format(kvPo.getCreateTime()));
            kvVo.setUpdateTime(simpleDateFormat.format(kvPo.getUpdateTime()));

            kvVos.add(kvVo);
            kvsMap.put(kvVo.getKey(),kvVo);
        }


        mv.getModel().put("project",project);
        mv.getModel().put("profile",profile);
        mv.getModel().put("profiles",profiles);
        mv.getModel().put("kvVos",kvVos);
        mv.getModel().put("kvmap", JSON.toJSONString(kvsMap));
        mv.setViewName("page/projectKv.ftl");

        return mv;
    }

    @RequestMapping("/addKvs")
    @ResponseBody
    public AjaxResponse addKvs(WebRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();

        try {
            String project = HtmlUtils.htmlEscape(webRequest.getParameter("project"));
            String[] profiles = webRequest.getParameterValues("profiles");
            String profile = HtmlUtils.htmlEscape(webRequest.getParameter("profile"));
            String key = HtmlUtils.htmlEscape(webRequest.getParameter("xkey"));
            String value = HtmlUtils.htmlEscape(webRequest.getParameter("xvalue"));
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

            for(String pf : profiles){
                KvPo temp = new KvPo();
                temp.setProject(project);
                temp.setProfile(pf);
                temp.setxKey(key);
                temp.setxValue(value);
                temp.setSecurity(security);
                temp.setDescription(description);

                kvPos.add(temp);
            }

            this.xKvService.addKvs(kvPos);

            ajaxResponse.setMsg("增加成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }

        return ajaxResponse;
    }

    @RequestMapping("editKv")
    @ResponseBody
    public AjaxResponse editKv(WebRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        try{
            String project = webRequest.getParameter("project");
            String profile = webRequest.getParameter("profile");
            String mkey = webRequest.getParameter("xkey");
            String value = HtmlUtils.htmlEscape(webRequest.getParameter("xvalue"));
            String description = HtmlUtils.htmlEscape(webRequest.getParameter("description"));

            String key = CommonUtil.genKeyByMkey(mkey);

            KvPo kvPo = new KvPo();
            kvPo.setProject(project);
            kvPo.setProfile(profile);
            kvPo.setxKey(key);
            kvPo.setxValue(value);
            kvPo.setDescription(description);

            this.xKvService.updateKv(kvPo);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }

        return ajaxResponse;
    }

    @RequestMapping("removeKv")
    @ResponseBody
    public AjaxResponse removeKv(WebRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();

        try{
            String mkey = webRequest.getParameter("xkey");
            this.xKvService.removeKvByMkey(mkey);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }

        return ajaxResponse;
    }
}
