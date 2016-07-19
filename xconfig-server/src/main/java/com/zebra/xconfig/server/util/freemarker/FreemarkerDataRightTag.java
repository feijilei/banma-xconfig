package com.zebra.xconfig.server.util.freemarker;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.util.Map;

/**
 * 用来判断权限的标签
 * @author zhengzhichao
 *
 */
public class FreemarkerDataRightTag implements TemplateDirectiveModel {

    @Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		//参数
		if(params.isEmpty()){
			throw new TemplateModelException("参数不能为空，需要角色列表编码(roles)");
		}
		
		TemplateModel valueModel = (TemplateModel)params.get("roles");
		
		if(valueModel == null){
			throw new TemplateModelException("角色列表(roles)不存在");
		}
		String roleStr = valueModel.toString();
        String[] needRoles = roleStr.split(",");
		
//		//当前用户拥有的权限
//		UserInfo userInfo = WebUtil.getSessionInfo();
//		if(userInfo != null){
//			List<String> roles = userInfo.getRoles();
//            for(String role : needRoles){
//                if(roles.contains(role)){//判断当前用户是否拥有这个权限
//                    body.render(env.getOut());
//                    break;
//                }
//            }
//		}
	}

}