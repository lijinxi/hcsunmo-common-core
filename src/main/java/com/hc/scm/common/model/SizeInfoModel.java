package com.hc.scm.common.model;

import java.io.Serializable;


/**
 * 尺码横排，行转列model（默认支持20列）
 * Description: 
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      liutao
 * Createdate:  2015-3-31上午10:39:59
 */
public class SizeInfoModel  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 尺码编码
	 */
	private String sizeNo;
	
	/**
	 * 尺码名称
	 */
	private String sizeName;
	
	/**
     * 尺码数量
     */
    private Integer sizeQty;

	public String getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(String sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public Integer getSizeQty() {
		return sizeQty;
	}

	public void setSizeQty(Integer sizeQty) {
		this.sizeQty = sizeQty;
	}
}
