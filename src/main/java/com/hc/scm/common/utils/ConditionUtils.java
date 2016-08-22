package com.hc.scm.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.hc.scm.common.model.QueryConditionReq;

public class ConditionUtils {
	
	private final static Map<String, String> operatorType=new HashMap<String, String>();
	
	static{
		operatorType.put("10", "=");
		operatorType.put("11", ">");
		operatorType.put("12", "<");
		operatorType.put("13", ">=");
		operatorType.put("14", "<=");
		operatorType.put("15", "LIKE");
		operatorType.put("16", "!=");
		operatorType.put("17", "is null");
		operatorType.put("18", "is not null");
	}
	
	/**
	 * 拼接条件
	 * @param list
	 * @return
	 */
	public static String getQueryCondition(List<QueryConditionReq> list,Map<String, Object> retParams ){
		StringBuffer sb=new StringBuffer();
		String fieldName="";
		for(QueryConditionReq qcr:list){
			fieldName=CommonUtil.convertJaveBeanStrToUnderLine(qcr.getProperty());
			sb.append(" AND ").append(fieldName)
			.append(" ").append(operatorType.get(qcr.getOperator())).append(getConditionValue(operatorType.get(qcr.getOperator()),qcr.getValue()));
			retParams.put(qcr.getProperty(), qcr.getValue());
		}
		return sb.toString();
	}

	private static String getConditionValue(String operator,String value){
		if(operator.equals("LIKE")){
			return   " '%"+value+"%'";
		}
		if(value==null)return "";
		if(value=="")return " ''";
		return  " '"+value+"'";
	}
	
}
