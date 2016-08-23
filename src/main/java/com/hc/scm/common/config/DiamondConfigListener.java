package com.hc.scm.common.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.starit.diamond.manager.impl.DefaultDiamondManager;

/**
 * 
 * Description: 数据库里的配置项目有更新时同步更新本地文件
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-4-8下午2:10:15
 */
public class DiamondConfigListener implements ServletContextListener {
	private static Logger logger = LoggerFactory.getLogger(DiamondConfigListener.class);
	private Map<String, DefaultDiamondManager> mList =  new HashMap<String, DefaultDiamondManager>();
	
	/**
	 * 配置文件的根路径
	 * 如：/etc/hcchccm/
	 * 	   D:/hcconfhc	
	 */
	private  String webConfigBaseDir;
	
	public  String getWebConfigBaseDir() {
		return webConfigBaseDir;
	}
	
	/**
	 * 监听的配置项目
	 * 格式:group:dataId,group:dataId 
	 * 如：hcc:uc-config,hchcjdbc-config
	 */
	private  String groupdataIdList;

	public  String getGroupdataIdList() {
		return groupdataIdList;
	}

	public  void setGroupdataIdList(String groupdataIdList) {
		this.groupdataIdList = groupdataIdList;
	}

	public void setWebConfigBaseDir(String webConfigBaseDir) {
		this.webConfigBaseDir = webConfigBaseDir;
	}

	public void init(){
		logger.debug("init() start...");
		String liststr=getGroupdataIdList();
		String list_arr[]=liststr.split(",");
		int list_size=list_arr.length;
		String group="", dataId="";
		String configBaseDir=getConfigBaseDir();
		try{
			for(int i=0;i<list_size;i++){
				String arr[]=list_arr[i].split(":");
				group=arr[0];
				dataId=arr[1];
				//创建订阅者
				DefaultDiamondManager m=new DefaultDiamondManager(group, dataId,
					new HcManagerListener(group,dataId,configBaseDir),true);
				
				if (m.exists(dataId,group)){
					mList.put(group, m);
				}
				else{
					m.close();
					m = null;
				}
			}
		}
		catch (Exception e) {
			logger.error("配置监听异常 error:",e);
		}
		
	}
	
	
	/** 
     * 获取配置文件路径
     * @return
     */  
    public  String getConfigBaseDir(){
    	String configBaseDir=getConfigPropertiesLocation();
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

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		 ServletContext sc = sce.getServletContext();
		 groupdataIdList=sc.getInitParameter("groupdataIdList");
		 if(StringUtils.isNotEmpty(sc.getInitParameter("webConfigBaseDir"))){
			 webConfigBaseDir=sc.getInitParameter("webConfigBaseDir");
		 }
		 
		 init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		 //关闭容器时销毁监听器
		 closeDiamondListener();
	}
	
	/**
	  * 关闭监听
	  */
	private void closeDiamondListener() {
		if (null != mList && mList.size() > 0) {
			// 移除diamond轮询
			for (DefaultDiamondManager d : mList.values()) {
				d.close();
			}
		}
	}
	
}
