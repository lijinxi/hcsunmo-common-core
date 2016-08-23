package com.hc.scm.common.shiro;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;

import com.hc.scm.common.config.DiamondConfigListener;

public class CommonInitializer implements WebApplicationInitializer {
	
	private static Logger logger = LoggerFactory.getLogger(CommonInitializer.class);

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		//String groupdataIdList = servletContext.getInitParameter("groupdataIdList");
		String casConfigFile = servletContext.getInitParameter("casConfigFile");
		
		String diamondSwitch=ConfigProps.getInstance(casConfigFile).getString("diamond.config.open");//配置同步开关
		logger.info("contextInitialized diamondSwitch:"+diamondSwitch);
		if (isOpen(diamondSwitch)) {
			diamondConfigInit(servletContext);
		}
		//第一处改动
		/*String javamelodySwitch=ConfigProps.getInstance(casConfigFile).getString("javamelody.open");//java监控开关
		logger.info("contextInitialized javamelodySwitch:"+javamelodySwitch);
		System.out.println("javamelody："+javamelodySwitch);
		System.out.println("javamelody："+isOpen(javamelodySwitch));
		if (isOpen(javamelodySwitch)) {
			javamelodyConfigInit(servletContext);
		}*/
		
		//pushletServletInit(servletContext);
	}
	
	/**
	 * 动态注册pushlet servlet
	 * @param servletContext
	 */
	/*private void pushletServletInit(ServletContext servletContext){
		logger.info("pushletServletInit FilterRegistration Dynamic start...");
		ServletRegistration.Dynamic pushletServlet = servletContext.addServlet("pushlet", nl.justobjects.pushlet.servlet.Pushlet.class);
		pushletServlet.setLoadOnStartup(2);
		pushletServlet.addMapping("/pushlet.srv");
	}*/
	
	/**
	 * 动态注册diamond的Listener
	 * @param servletContext
	 * @throws ServletException
	 */
	public void diamondConfigInit(ServletContext servletContext)
			throws ServletException {
			logger.info("diamondConfigInit FilterRegistration Dynamic start...");
            servletContext.addListener(DiamondConfigListener.class);
	}
	
	/**
	 * 动态注册javamelody的Listener, filter
	 * @param servletContext
	 * @throws ServletException
	 */
	public void javamelodyConfigInit(ServletContext servletContext)
			throws ServletException {
			logger.info("javamelodyConfigInit FilterRegistration Dynamic start...");
			String filterMapping = ConfigProps.getFilterMapping();
          //  FilterRegistration.Dynamic monitoringFilter = servletContext.addFilter("MonitoringFilter", MonitoringFilter.class);
           // monitoringFilter.addMappingForUrlPatterns(null, false, filterMapping);
           // servletContext.addListener(SessionListener.class);
	}
	
	public boolean isOpen(String openStr) {
    	boolean flag=false;
    	try{
    		flag=Boolean.parseBoolean(openStr);
    	}catch(Exception ex){
    		flag=false;
    	}
    	
        return flag;
    }

}
