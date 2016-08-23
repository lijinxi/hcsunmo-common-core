package com.hc.scm.common.config;

import java.io.IOException;

import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.starit.diamond.manager.ManagerListener;

/**
 * DataID对应的配置信息，监听器实现
 */
public class HcManagerListener implements ManagerListener{
	private static final Log logger = LogFactory.getLog(HcManagerListener.class);
	private String group;
	private String dataId;
	private String configBaseDir;
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public HcManagerListener(String group, String dataId,String configBaseDir){
		this.group = group;
		this.dataId = dataId;
		this.configBaseDir=configBaseDir;
	}

	@Override
	public Executor getExecutor() {
		return null;
	}

	@Override
	public void receiveConfigInfo(String configInfo) {
		// 客户端处理数据的逻辑
		logger.debug("configInfo:"+configInfo);
		logger.debug("os.name:"+System.getProperty("os.name")+" configBaseDir:"+configBaseDir+" group:"+group+" dataId:"+dataId);
		ConfigInfo tmp=new ConfigInfo();
		tmp.setContent(configInfo);
		tmp.setGroup(group);
		tmp.setDataId(dataId);
		try {
			//配置有更新同步更新本地文件
			DiskServiceUtils.getInstance(configBaseDir).saveToDisk(tmp);
		} catch (IOException e) {
			logger.error("error:",e);
			throw new RuntimeException("配置有更新同步更新本地文件错误：", e);
		}
	}
	
}
