package com.hc.scm.common.utils;


import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * 读取配置文件的值
 * Description: 
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      peng.hz
 * Createdate:  2015-4-7下午5:24:45
 *
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-4-7     	peng.hz
 */
public class PropertiesUtils {
	
	/**
	 * 获得配置文件的值
	 * @param key
	 * @return
	 */
	public static String getPropertieValue(String key){
		Properties pfb=(Properties)SpringComponent.getBean("configProperties");
		try {
			return pfb.getProperty(key);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获得配置文件的值
	 * @param key
	 * @return
	 */
	public static String getString(String key){
		try {
			return getPropertieValue(key);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获得配置文件的值
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key){
		try {
			return Boolean.parseBoolean(getPropertieValue(key));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获得配置文件的值
	 * @param key
	 * @return
	 */
	public static int getInteger(String key){
		try {
			return Integer.parseInt(getPropertieValue(key));
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * 获得配置文件的值，配置为空时，返回defaultVal
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public static String getString(String key,String defaultVal){
		try {
			return StringUtils.isNotEmpty(getPropertieValue(key))? getPropertieValue(key):defaultVal;
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 获得配置文件的值，配置为空时，返回defaultVal
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public static boolean getBoolean(String key,boolean defaultVal){
		try {
			return StringUtils.isNotEmpty(getPropertieValue(key))? Boolean.parseBoolean(getPropertieValue(key)):defaultVal;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获得配置文件的值，配置为空时，返回defaultVal
	 * @param key
	 * @param defaultVal
	 * @return
	 */
	public static int getInteger(String key,int defaultVal){
		try {
			return StringUtils.isNotEmpty(getPropertieValue(key))? Integer.parseInt(getPropertieValue(key)):defaultVal;
		} catch (Exception e) {
			return -1;
		}
	}
	
}
