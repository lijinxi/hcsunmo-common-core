package com.hc.scm.common.utils;

/**
 * @author wgy 返回状态模型
 * 
 */
public class ResultModel {    
    /**
     * 返回信息码
     * 0：成功，其它:错误码
     */
    private String resultCode="0";
    /**
     * 返回信息
     */
    private String msg;

    /**
     * 响应返回数据对象
     */
    private Object retData;
    
    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getRetData() {
		return retData;
	}

	public void setRetData(Object retData) {
		this.retData = retData;
	}
}
