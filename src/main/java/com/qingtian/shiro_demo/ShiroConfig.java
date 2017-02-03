package com.qingtian.shiro_demo;

import java.util.LinkedHashMap;


import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.pac4j.core.client.Clients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zrk.oauthclient.client.QqClient;
import com.zrk.oauthclient.client.SinaWeiboClient;
import com.zrk.oauthclient.client.WeiXinClient;
import com.zrk.oauthclient.shiro.support.ClientFilter;

@Configuration
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

	//第三方登录client配置
	@Bean
	public Clients getClients(){
		SpringShiroAutoconfig config = getShiroConfig();
		QqClient qqClient = new QqClient(config.getQqKey(),config.getQqSecret());
		WeiXinClient weiXinClient = new WeiXinClient(config.getWeixinKey(),config.getWeixinSecret());
		SinaWeiboClient sinaWeiboClient = new SinaWeiboClient(config.getWeiboKey(),config.getWeiboSecret());
		Clients clients = new Clients(config.getOauthCallback(),qqClient,weiXinClient,sinaWeiboClient);
		return clients;
	}
	
	@Bean(name="memoryCacheManager")
	public MemoryConstrainedCacheManager getMemoryCacheManager() {
		return new MemoryConstrainedCacheManager();
	}
	@Bean(name="sessionDAO")
	public SessionDAO getSessionDAO() {
		return new MemorySessionDAO();
	}
	@Bean(name="sessionManager")
	public DefaultWebSessionManager getDefaultWebSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionDAO(getSessionDAO());
		sessionManager.setGlobalSessionTimeout(30 * 60 * 1000);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setCacheManager(getMemoryCacheManager());
		return sessionManager;
	}
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager defaultWebSecurityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(getShiroDbAndClientRealm());
		securityManager.setCacheManager(getMemoryCacheManager());
		securityManager.setSessionManager(getDefaultWebSessionManager());
		return securityManager;
	}
	@Bean(name = "shiroDbAndClientRealm")
	public ShiroDbAndClientRealm getShiroDbAndClientRealm(){
		ShiroDbAndClientRealm realm = new ShiroDbAndClientRealm();
		realm.setClients(getClients());
		return realm;
	}
	@Bean
	public SpringShiroAutoconfig getShiroConfig(){
		return new SpringShiroAutoconfig();
	}
}
