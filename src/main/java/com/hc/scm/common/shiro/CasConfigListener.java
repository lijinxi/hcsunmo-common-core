package com.hc.scm.common.shiro;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * 
 * Description:cas filter动态注册
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-4-8下午2:10:15
 */
public class CasConfigListener implements ServletContextListener {
	private static Logger logger = LoggerFactory.getLogger(CasConfigListener.class);
	private String casConfigFile;
	
    public String getCasConfigFile() {
		return casConfigFile;
	}

	public void setCasConfigFile(String casConfigFile) {
		this.casConfigFile = casConfigFile;
	}

	/**
	 * 动态注册cas的Listener及filter
	 * @param servletContext
	 * @throws ServletException
	 */
	public void casConfigInit(ServletContext servletContext)
			throws ServletException {
			logger.info("casConfigInit FilterRegistration Dynamic start...");
            String casServerUrlPrefix = ConfigProps.getCasServerUrlPrefix();
            String casServerLoginUrl = ConfigProps.getCasServerLoginUrl();
            String serverName = ConfigProps.getServerName();
            String filterMapping = ConfigProps.getFilterMapping();
            String excludePaths = ConfigProps.getString("sso.excludePaths");//不拦截的url
            
            servletContext.addListener(SingleSignOutHttpSessionListener.class);
 
            FilterRegistration.Dynamic singleSignOutFilter = servletContext.addFilter("SingleSignOutFilter", SingleSignOutFilter.class);
            singleSignOutFilter.setInitParameter("casServerUrlPrefix", casServerUrlPrefix);
            singleSignOutFilter.addMappingForUrlPatterns(null, false, filterMapping);
 
            FilterRegistration.Dynamic authenticationFilter = servletContext.addFilter("AuthenticationFilter", com.hc.scm.common.shiro.AuthenticationFilter.class);
            authenticationFilter.setInitParameter("casServerLoginUrl", casServerLoginUrl);
            authenticationFilter.setInitParameter("serverName", serverName);
            authenticationFilter.setInitParameter("excludePaths", excludePaths);
            authenticationFilter.addMappingForUrlPatterns(null, false, filterMapping);
 
            FilterRegistration.Dynamic ticketValidationFilter = servletContext.addFilter("TicketValidationFilter", Cas20ProxyReceivingTicketValidationFilter.class);
            ticketValidationFilter.setInitParameter("casServerUrlPrefix", casServerUrlPrefix);
            ticketValidationFilter.setInitParameter("serverName", ConfigProps.getServerName());
            ticketValidationFilter.addMappingForUrlPatterns(null, false, filterMapping);
 
            FilterRegistration.Dynamic requestWrapperFilter = servletContext.addFilter("RequestWrapperFilter", HttpServletRequestWrapperFilter.class);
            requestWrapperFilter.addMappingForUrlPatterns(null, false, filterMapping);
 
            FilterRegistration.Dynamic assertionThreadLocalFilter = servletContext.addFilter("AssertionThreadLocalFilter", AssertionThreadLocalFilter.class);
            assertionThreadLocalFilter.addMappingForUrlPatterns(null, false, filterMapping);
            
           // FilterRegistration.Dynamic shiroFilter = servletContext.addFilter("shiroFilter", DelegatingFilterProxy.class);
            //shiroFilter.addMappingForUrlPatterns(null, false, filterMapping);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		 ServletContext sc = sce.getServletContext();
		try {
			casConfigFile=sc.getInitParameter("casConfigFile");
			boolean isSSO=ConfigProps.getInstance(casConfigFile).getSSO();
			logger.info("contextInitialized isSSO:"+isSSO);
			if (isSSO) {
				casConfigInit(sc);
			}
		} catch (ServletException e) {
			logger.error("error:",e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
}
