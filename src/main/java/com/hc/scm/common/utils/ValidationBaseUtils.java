package com.hc.scm.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hc.scm.common.base.dal.BaseCrudDao;
import com.hc.scm.common.constans.SysConstans;
import com.hc.scm.common.model.ImportValidationCondition;
import com.hc.scm.common.model.QueryConditionReq;

public class ValidationBaseUtils<T> {
	
	/**
	 *公共验证
	 * @param t
	 * @param validationList
	 * @return 返回错误信息，如果没有错误信息则返回空字符串
	 */
	public static <T> String validationImport(T t,List<ImportValidationCondition> validationList){
		BaseCrudDao baseCrudDao;
		String modelName;
		StringBuffer sb= new StringBuffer();
		Map<String,Object> params=new HashMap<String, Object>();
		for(ImportValidationCondition ivc:validationList){//验证唯一性
			if(ivc.getValidationType().equals(SysConstans.UNIQUE)){
				modelName=CommonUtil.changeFirstCharUporLow(ivc.getValidationModel(), 1)+"Dao";
				baseCrudDao=(BaseCrudDao)SpringComponent.getBean(modelName);
				params= getQueryCondition(ivc.getConditionValue(),t);
				int count= baseCrudDao.findCount(params);
				if(count>0) sb.append(params.toString()).append(" :违反唯一约束      ");
			}
			else if(ivc.getValidationType().equals(SysConstans.IS_EXIST)){
				modelName=CommonUtil.changeFirstCharUporLow(ivc.getValidationModel(), 1)+"Dao";
				baseCrudDao=(BaseCrudDao)SpringComponent.getBean(modelName);
				params= getQueryCondition(ivc.getConditionValue(),t);
				int count= baseCrudDao.findCount(params);
				if(count==0) sb.append(params.toString()).append(" :不存在，验证错误     ");
			}
		}
		return sb.toString();
	}
	
	private static <T> Map<String,Object> getQueryCondition(String conditionValue,T t){
		Map<String,Object> params=new HashMap<String, Object>();
		String [] uniques=conditionValue.split(",");
		List<QueryConditionReq> listCondition=new ArrayList<QueryConditionReq>();
		QueryConditionReq queryConditionReq;
		Object objValue;
		for(String unique:uniques){
			queryConditionReq=new QueryConditionReq();
			objValue=CommonUtil.getFieldValue(t, unique, t.getClass());
			queryConditionReq.setValue(objValue.toString());
			queryConditionReq.setProperty(unique);
			queryConditionReq.setOperator("10");
			listCondition.add(queryConditionReq);
		}
		String queryCondition= ConditionUtils.getQueryCondition(listCondition,params);
		params.put("queryCondition", queryCondition);
		return params;
	}

}
