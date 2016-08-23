package com.hc.scm.common.utils;

import com.alibaba.fastjson.JSON;

public class JsonUtils {
	
	public static String getValueByKey(String key,String retVal){
		try{
			return JSON.parseObject(retVal).get(key).toString();
		}catch (NullPointerException e) {
			return "";
		}
	}

}
