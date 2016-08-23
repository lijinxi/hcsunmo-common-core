package com.hc.scm.common.shiro;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hc.scm.common.model.SystemUser;
import com.hc.scm.common.utils.LoginUtils;

/**
 * Description: 操作权限验证过滤
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-5-12下午2:24:13
 */
public class ShiroFilter implements Filter {
	protected Logger logger = LoggerFactory.getLogger(ShiroFilter.class);
	@Resource
	private ShiroSession ShiroSession;
	private String excludePath; //shiro filter不验证的路径
	
	
	public String getExcludePath() {
		return excludePath;
	}

	public void setExcludePath(String excludePath) {
		this.excludePath = excludePath;
	}

	public void destroy() {
	}
	
	/**
	 * ShiroFilter过滤器
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Principal principal = httpRequest.getUserPrincipal();
		String reqURI=httpRequest.getRequestURI();
		Subject subjects = SecurityUtils.getSubject();
		if(reqURI.contains("/logout.json")){
			subjects.logout();
		}
		
		if (logger.isDebugEnabled()){
			logger.debug("RequestURI:"+reqURI);
    	}
		
		//静态资源及不需要过滤的url
		if(LoginUtils.checkExcludeUrls(reqURI)){
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		
		try {
			if (principal != null) {
				SystemUser user = ShiroSession.getUserByLoginNamePassword(principal.getName(), null);
				if (user != null) {
					
					//非超级管理员需要过滤操作权限
					if(user.getSuAdminRoleId()!=null&&user.getSuAdminRoleId()==0&&LoginUtils.filterUnauthorizedRequest(httpRequest, httpResponse,user.getUserMenuMap())){
						return; //没有操作权限，直接返回。
					}
					
					UsernamePasswordToken token = new UsernamePasswordToken(user.getUserCode(), user.getPassword());
					subjects = SecurityUtils.getSubject();
					subjects.login(token);
					//session续期
					ShiroSession.saveLoginUserSession(user);
				}
				else {
					// 如果用户为空，则subjects信息登出
					if (subjects != null) {
						subjects.logout();
					}
				}
			}
		} catch (Exception e) {
			logger.error("doFilter",e);
		}
		chain.doFilter(httpRequest, httpResponse);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
	
}