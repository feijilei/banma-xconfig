package com.zebra.xconfig.server.util;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 此类作为一个工具类，实现ApplicationContextAware接口，spring初始化时候自动设置ApplicationContext
 * 可以调用此类获取spring当前ioc容器上下文，完成程序中动态加载bean等操作！
 * @author ying
 *
 */
public class SpringAppContext implements ApplicationContextAware {
	public static ApplicationContext springAppContext;
	
	@Override
	public void setApplicationContext(ApplicationContext springAppContext)
			throws BeansException {
		SpringAppContext.springAppContext = springAppContext;
	}
	/**
	 * 提供一个获取spring ApplicationContext 对象的方法
	 * @return
	 */
	public static ApplicationContext getSpringAppContext(){
		return SpringAppContext.springAppContext;
	}
	/**
	 * 提供一个动态获取Bean的方法！
	 * @param beanId
	 * @return
	 * @throws org.springframework.beans.BeansException
	 */
	@SuppressWarnings("unchecked")
	public static <T>T getBean(String beanId) throws BeansException{
		return (T)SpringAppContext.springAppContext.getBean(beanId);
	}
	
	public static <T>T getBean(Class<T> arg){
		return (T)springAppContext.getBean(arg);
	}

}