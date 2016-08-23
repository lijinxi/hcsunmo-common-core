package com.hc.scm.common.pushlet;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 推送消息封装类
 * Description: 
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      zhu.liang
 * Createdate:  2015-5-11上午9:10:16
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-5-11     	zhu.liang
 */
public class PushMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9126004852950094577L;
	
	/**
	 * 消息id
	 */
	private long id;
	
	 /**
     * 类型
     */
    private String type;
	
	/**
     * 用户id
     */
    private Integer userId;
    
    /**
     * 作业id
     */
    private long jobId;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息生成时间
     */
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
