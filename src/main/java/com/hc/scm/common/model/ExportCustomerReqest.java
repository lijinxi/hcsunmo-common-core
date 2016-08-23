package com.hc.scm.common.model;

/**
 * 导出从表
 * Description: 
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      peng.hz
 * Createdate:  2015-3-28上午10:09:57
 *
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-3-28     	peng.hz
 */
public class ExportCustomerReqest {
	
	/**
	 * 从表名称
	 */
	private String customerName;
	
	private String sheetName;
	/**
	 * json格式的字符串数组
	 */
	private String columnsList;
	
	private String customerModle;

	

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getColumnsList() {
		return columnsList;
	}

	public void setColumnsList(String columnsList) {
		this.columnsList = columnsList;
	}

	public String getCustomerModle() {
		return customerModle;
	}

	public void setCustomerModle(String customerModle) {
		this.customerModle = customerModle;
	}
	
	
	

}
