package com.qingtian.shiro_demo;

import java.util.LinkedHashMap;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.context.annotation.Bean;

import com.zrk.oauthclient.shiro.support.ClientFilter;


public class ShiroConfig {
	//shiro过滤器
	@Bean(name="shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		SpringShiroAutoconfig config = getShiroConfig();
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
		//第三方登陆回调过滤器
		ClientFilter clientFilter =  new ClientFilter();
		clientFilter.setClients(getClients());//
		clientFilter.setRedirectAfterSuccessfulAuthentication(true);
		Map<String, Filter> filterMap = new LinkedHashMap<String,Filter>();
		//定义第三方回调过滤器
		filterMap.put("client", clientFilter);
		Map<String, String> filterChainMap =  new LinkedHashMap<String, String>();
		for (String path : config.getFilter()){
			if(path!=null&&!"".equals(path)){
				String[] kv = path.split("=");
				filterChainMap.put(kv[0].trim(), kv[1].trim());
			}
		}
		shiroFilterFactoryBean.setFilters(filterMap);
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
		shiroFilterFactoryBean.setLoginUrl(config.getLoginUrl());
		shiroFilterFactoryBean.setSuccessUrl(config.getSuccessUrl());
		shiroFilterFactoryBean.setUnauthorizedUrl(config.getUnauthorizedUrl());
		return shiroFilterFactoryBean;
	}
}
