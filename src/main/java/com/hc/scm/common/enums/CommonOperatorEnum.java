package com.hc.scm.common.enums;

/**
 * Description: 公共的操作
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-3上午11:57:56
 */
public enum CommonOperatorEnum {
	
	DELETED("deleted"),
	UPDATED("updated"),
	INSERTED("inserted");
	
	private String operator;
	
	CommonOperatorEnum(String operator){
		this.operator=operator;
	}

	public String getOperator() {
		return operator;
	}
}
