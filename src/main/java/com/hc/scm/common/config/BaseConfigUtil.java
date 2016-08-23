package com.hc.scm.common.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Description: 基础配置文件类
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-4-8下午2:10:15
 */
public class BaseConfigUtil {
	private static Logger logger = LoggerFactory.getLogger(BaseConfigUtil.class);
	
	/**
	 * 单例模式
	 */
	private static BaseConfigUtil instance = new BaseConfigUtil();

	private BaseConfigUtil() {
	}

	public static BaseConfigUtil getInstance() {
		return instance;
	}
	
	/**
	 * 项目名称
	 * 如：hcc、hchc
	 */
	private String webAppRootKey;
	
	/**
	 * 配置文件的根路径
	 * 如：/etc/hcchccm/
	 * 	   D:/hcconfhc	
	 */
	private  String webConfigBaseDir;
	
	public  String getWebConfigBaseDir() {
		return webConfigBaseDir;
	}
	

	public void setWebConfigBaseDir(String webConfigBaseDir) {
		this.webConfigBaseDir = webConfigBaseDir;
	}

	public String getWebAppRootKey() {
		return webAppRootKey;
	}

	public void setWebAppRootKey(String webAppRootKey) {
		this.webAppRootKey = webAppRootKey;
	}

	
	/** 
     * 获取配置文件路径
     * @return
     */  
    public  String getConfigBaseDir(){
    	String configBaseDir=getConfigPropertiesLocation()+webAppRootKey;
    	logger.debug("configBaseDir:"+configBaseDir);
		return configBaseDir;
    }
    
	/** 
     * 获取配置文件路径
     * @return
     */  
    public  String getConfigPropertiesLocation(){
    	String osname=getOSname();
    	String path="";
    	if(StringUtils.isEmpty(getWebConfigBaseDir())){
    		if(osname.equals("Windows")){
    			path="D:/hcconf/scm/";
    		}
    		else{
    			path="/etc/hcconf/scm/";
    		}
    	}
    	else{
    		path=getWebConfigBaseDir();
    	}
		return path;
    }
    
    
	/** 
     * 获取操作系统名字 
     * @return 操作系统名 
     */  
    public  String getOSname(){
    	String osname=System.getProperty("os.name");
    	if(osname.startsWith("win") || osname.startsWith("Win")){
    		osname="Windows";
    	}
    	else{
    		osname="Linux";
    	}
		return osname;
    }

}
