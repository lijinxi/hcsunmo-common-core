package com.hc.scm.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.alibaba.fastjson.JSON;



public class SizeHorizontalResolve {
	
	private static Logger logger = LoggerFactory.getLogger(SizeHorizontalResolve.class);
	
	/**
	 * 尺码横排解析
	 * @param sizeHorizontalJson   尺码横排josn字符串对象
	 * @param detailClazz 明细类
	 * @param type O:尺码横排，1：物料新旧替换 默认为尺码横排
	 * @param qtyProperty 尺码数量需要设置的字段，默认为sizeQty
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List resolveSizeHorizontalFromJson(String sizeHorizontalJson,Class<?> detailClazz,HashMap<String, Map> sizeTypeMap,String type,String qtyProperty){
		List<Object> list=new ArrayList<Object>();
		for(int i=1;i<=20;i++){
			try {
				String insteadSizeType=JsonUtils.getValueByKey("insteadSizeType", sizeHorizontalJson);
				String sizeCountField= JsonUtils.getValueByKey("f"+i, sizeHorizontalJson);
				if(StringUtils.isBlank(sizeCountField)|| sizeCountField.equals("0"))continue;
				String sizeTypeNo=JsonUtils.getValueByKey("sizeTypeNo", sizeHorizontalJson);
				if(!sizeTypeMap.containsKey(sizeTypeNo)){
					   List<String> sizeObjList=queryBasSize(sizeTypeNo);
					   HashMap<String, String> sizeMap=new HashMap<String,String>();
					   for(String sizeObj:sizeObjList){
						  String sizeNo=JsonUtils.getValueByKey("sizeNo", sizeObj);
					      sizeMap.put("f"+JsonUtils.getValueByKey("orderNo", sizeObj),sizeNo);
					      if(StringUtils.isNotBlank(type) && type.equals("1")){
					    	  sizeMap.put(sizeTypeNo+JsonUtils.getValueByKey("sizeCode", sizeObj),sizeNo);
					      }
					    }
					   sizeTypeMap.put(sizeTypeNo, sizeMap);
				}
				if(StringUtils.isNotBlank(type) && type.equals("1")){
					  if(!sizeTypeMap.containsKey(insteadSizeType)){
						  List<String> newsizeObjList=queryBasSize(insteadSizeType);
						  HashMap<String, String> sizeMap=new HashMap<String,String>();
						  for(String sizeObj:newsizeObjList){
							  String sizeNo= JsonUtils.getValueByKey("sizeNo", sizeObj);
							  sizeMap.put("f"+JsonUtils.getValueByKey("orderNo", sizeObj),sizeNo);
							  sizeMap.put(insteadSizeType+JsonUtils.getValueByKey("sizeCode", sizeObj),sizeNo);
						  }
						 sizeTypeMap.put(insteadSizeType, sizeMap);
				    }
				}
				String sizeNo=sizeTypeMap.get(sizeTypeNo).get("f"+i).toString();
				Object obj=JSON.parseObject(sizeHorizontalJson, detailClazz);
			//	obj.getClass().getDeclaredField(sizeTypeNo).getGenericType()
				CommonUtil.setFieldValue(obj, "sizeNo", detailClazz, sizeNo);
				if(StringUtils.isNotBlank(type) && type.equals("1")){
					CommonUtil.setFieldValue(obj, "sizeValue",detailClazz,sizeTypeMap.get(insteadSizeType).
								get(insteadSizeType+sizeCountField));//设置为字符串的
				}else{
					
					if(StringUtils.isNotBlank(qtyProperty)){
						CommonUtil.setFieldValue2(obj, qtyProperty, detailClazz,sizeCountField);//
					}else{
						Object sizeQty=Integer.valueOf(sizeCountField);
						CommonUtil.setFieldValue(obj, "sizeQty", detailClazz,sizeQty);//设置为数字的
					}
				}
				list.add(obj);
				
			} catch (Exception e) {
				logger.error("解析尺码信息失败:", e);
				return null;
			}

		}
		return list;
	};
	
	private static  List<String> queryBasSize(String sizeTypeNo) {
		try {
			String url = "http://"+CommonUtil.getHostUrl()+"/hcdm-web/bas_size/listAll.json";
			HashMap<String, String> params=new HashMap<String, String>();
			params.put("sizeTypeNo", sizeTypeNo);
			String str = HttpUtils.post(url, params);
			String result=JsonUtils.getValueByKey("result", str);
			String reusltCode=JsonUtils.getValueByKey("resultCode", result);
			if(reusltCode.equals("0")){
		    	String listsize=JsonUtils.getValueByKey("list", str);
		    	return   JSON.parseArray(listsize,String.class);
			}
		}catch (Exception e) {
			logger.error("获取尺码信息失败:", e);
			return null;
		}
		return null;
	}
	
	 
}
