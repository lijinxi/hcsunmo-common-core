package com.hc.scm.common.pushlet;

import java.util.HashMap;

/**
 * Description: 消息类型
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-5-12下午3:14:52
 */
public class MessageType{
	
	private static HashMap<String, String> typemap=new HashMap<String, String> ();
	static{
		typemap.put("0", "UC系统消息");
		typemap.put("1", "PD系统消息");
		typemap.put("2", "SD系统消息");
		typemap.put("3", "PP系统消息");
		typemap.put("4", "MM系统消息");
		typemap.put("5", "QM系统消息");
		typemap.put("6", "FA系统消息");
		typemap.put("7", "MDM系统消息");
	}
	
	public static HashMap<String, String> getTypemap() {
		return typemap;
	}
	
	public static void setTypemap(HashMap<String, String> typemap) {
		MessageType.typemap = typemap;
	}
	
	public static String  getMsgTyp(String key) {
		return typemap.get(key);
	}
	
}
