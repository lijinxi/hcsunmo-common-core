package com.hc.scm.common.utils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.alibaba.fastjson.JSON;
import com.hc.scm.common.constans.SysConstans;
import com.hc.scm.common.model.SystemUser;

public class BillUtils {
	private static Logger logger = LoggerFactory.getLogger(BillUtils.class);
	
	/**
	 * 获得单据编号
	 * @billType 单据类型
	 * @detail 对象的json字符串
	 * @return
	 */
	public static String getBillNo(String billType,String objJson){
		 //String url = "http://172.20.50.73:8080/hcdm-web/bas_system_code/getSheetIdCode.json";
		  String url = "http://"+CommonUtil.getHostUrl()+"/hchc-web/bas_system_code/getSheetIdCode.json";
	      HashMap<String,String> params = new HashMap<String,String>();
	      String ret;
	      params.put("billtypeNo",billType);
	      if(StringUtils.isNotBlank(objJson)){
	    	  params.put("detail", objJson);
	      }
			try {
				logger.info(" request mdm getSheetIdCode");
				logger.info("params:"+params);
				logger.info("url:"+url);
				String str = HttpUtils.post(url, params);
				logger.info("getSheetIdCode  return :"+str);
			    String result=JsonUtils.getValueByKey("result", str);
			    String reusltCode=JsonUtils.getValueByKey("resultCode", result);
			    if(reusltCode.equals("0")){
			    	ret= JsonUtils.getValueByKey("sheetIdCode", str);
			    	if(StringUtils.isBlank(ret)){
			    		logger.error("获取单据失败:", str);
			    		return null;
			    	}
			    	return ret;
			    }else{
			    	logger.error("获取单据失败:", str);
			    }
			} catch (IOException e) {
				logger.error("获取单据失败:", e);
			}
	       return null;
	}
	
	/**
	 * 获取单据号
	 * @param billType  单据类型
	 * @param obj 具体对象
	 * @return
	 */
	public static String getBillNo(String billType,Object obj){
		String objJson="";
		if(obj!=null){
			objJson=JSON.toJSONString(obj);
		}
		return getBillNo( billType, objJson);
	}
	
	/**
	 * 设置单据号及编辑人和时间等
	 * @param obj
	 * @param fieldname
	 * @param clazz
	 * @param systemUser
	 * @param date
	 */
	public static void setBillNoAndCreator(Object obj,
			Class<?> clazz,SystemUser systemUser,Date date,String billNo){
		List<String> fieldList=CommonUtil.getFieldNames(obj);
		if(CommonUtil.isContainsField(fieldList, SysConstans.BIll_NO)){
			CommonUtil.setFieldValue(obj, SysConstans.BIll_NO, clazz, billNo);
		}
		if(CommonUtil.isContainsField(fieldList,SysConstans.CREATOR_FIELDNAME)){
		   CommonUtil.setEntityDefaultField(obj, 0, systemUser,clazz,date);//设置时间与编辑人
		}
	}

}
