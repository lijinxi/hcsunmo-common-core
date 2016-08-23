package com.hc.scm.common.model;

import java.util.List;

/**
 * 物料尺码横排-前端尺码横排的编辑model
 * Description: 
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      liutao
 * Createdate:  2015-4-14下午1:54:15
 */
public class MaterialSizeRowColWrapModel extends SizeRowColWrapModel {
	private static final long serialVersionUID = 1L;

    /**
     * 物料编号
     */
    private String materialNo;
    /**
     * 物料编码
     */
    private String materialCode;
    
    /**
     * 物料名称
     */
    private String materialName;
    
    private List<SizeInfoModel> sizeInfo;

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	
	
	public List<SizeInfoModel> getSizeInfo() {
		return sizeInfo;
	}

	public void setSizeInfo(List<SizeInfoModel> sizeInfo) {
		this.sizeInfo = sizeInfo;
	}

}