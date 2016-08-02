package com.zebra.xconfig.server.util.freemarker;

import com.zebra.xconfig.server.web.Intercepter.UrlResouces;
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
		
		if(params.isEmpty() || params.size() != 2){
			throw new TemplateModelException("参数不合法，需要角色编码(role)和资源路径(url)");
		}
		
		TemplateModel role = (TemplateModel)params.get("role");
		if(role == null){
			throw new TemplateModelException("角色编码(role)不存在");
		}
		String roleStr = role.toString();
        int curRole = Integer.valueOf(roleStr);

        TemplateModel urlModel = (TemplateModel)params.get("url");
        String url = urlModel.toString();

        int needRole = UrlResouces.getResouceRole(url);
        if(curRole >= needRole){
            body.render(env.getOut());
        }
	}
}