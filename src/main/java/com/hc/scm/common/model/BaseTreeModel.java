package com.hc.scm.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      liu.tao
 * Createdate:  2015-1-28下午2:52:19
 *
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-1-28     	liu.tao
 */
public class BaseTreeModel<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;	//树节点ID
	private String text;	//树节点名称
	private String parentId;	//父节点
	private String expanded ;	//是否展开
	private String leaf;	//是否叶子节点 
	List<T> children;	//子节点
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getExpanded() {
		return expanded;
	}
	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}
	public String getLeaf() {
		return leaf;
	}
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	public List<T> getChildren() {
		return children;
	}
	public void setChildren(List<T> children) {
		this.children = children;
	}
	
}
