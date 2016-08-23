package com.hc.scm.common.model;

import java.util.List;
import java.util.Map;

public class TreeModel {
	
	private String id;	//树节点ID
	private String text;	//树节点名称
	private String parentId;	//父节点
	private String expanded ;	//是否展开
	private String leaf;	//是否叶子节点 
	List<TreeModel> children;	//子节点
	private Map<String,Object> others; //其他信息
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
	public List<TreeModel> getChildren() {
		return children;
	}
	public void setChildren(List<TreeModel> children) {
		this.children = children;
	}
	public Map<String, Object> getOthers() {
		return others;
	}
	public void setOthers(Map<String, Object> others) {
		this.others = others;
	}
	
	

}
