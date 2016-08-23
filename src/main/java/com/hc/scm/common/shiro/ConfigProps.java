package com.hc.scm.common.shiro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hc.scm.common.config.BaseConfigUtil;
 
public class ConfigProps{
 
    private static final Logger logger = LoggerFactory.getLogger(ConfigProps.class);
 
    private static final Properties configProps = new Properties();
    
    /**
     * 默认的配置文件
     * 当web.xml没有配置casConfigFile时，调用该配置文件
     */
    private static final String DefaultConfigFile = "hc-common/common-config.properties";
    
    private String configFile;
 
    public String getConfigFile() {
    	return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	/**
	 * 单例模式
	 */
	private static ConfigProps instance = new ConfigProps();

	private ConfigProps() {
	}

	public static ConfigProps getInstance(String configFile) {
		configFile=StringUtils.isNotEmpty(configFile)? configFile+".properties":DefaultConfigFile;
		if(configFile.startsWith("/")){
			configFile=configFile.substring(1);
		}
		instance.configFile=BaseConfigUtil.getInstance().getConfigPropertiesLocation()+configFile;
        try {
        	logger.info("ConfigProps load file:"+instance.configFile);
            configProps.load(new FileInputStream(new File(instance.configFile)));
        } catch (IOException e) {
            logger.error("加载属性文件出错！", e);
        }
		return instance;
	}

    public static boolean isSSO() {
        return Boolean.parseBoolean(configProps.getProperty("sso"));
    }
    
    public boolean getSSO() {
    	boolean flag=false;
    	if(configProps!=null&&configProps.getProperty("sso")!=null){
    		flag=Boolean.parseBoolean(configProps.getProperty("sso"));
    	}
    	
        return flag;
    }
    
    public static String getString(String key) {
        return configProps.getProperty(key) == null ? "" : configProps.getProperty(key);
    }
 
    public static String getCasServerUrlPrefix() {
        return configProps.getProperty("sso.casServerUrlPrefix");
    }
 
    public static String getCasServerLoginUrl() {
        return configProps.getProperty("sso.casServerLoginUrl");
    }
 
    public static String getServerName() {
        return configProps.getProperty("sso.serverName");
    }
 
    public static String getFilterMapping() {
        return configProps.getProperty("sso.filter_mapping");
    }
}