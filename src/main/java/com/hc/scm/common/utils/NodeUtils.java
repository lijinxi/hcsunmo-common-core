package com.hc.scm.common.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.hc.scm.common.base.service.BaseCrudService;

/**
 * Description:节点工具辅助类,借助它展开所有节点数据,适用于子父节点在同一个表里的情况 
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * @author:     lucheng
 * @date:  2015-4-3 下午8:28:11
 * @version 1.0.0
 */
public class NodeUtils<ModeType> {
	private BaseCrudService basCrudService;
	private String[] linkFields; 

	@SuppressWarnings("unused")
	private NodeUtils() {

	}

	/**
	 * 节点工具的构造器
	 * @param basCrudService  传递对应的Service
	 * @param linkFields 
	 * 支持下面的几种模式，所指定的field必须都要有get方法:
	 * 模式一：new String[] {"billNo","orderNo"}意思是将billNo和orderNo的值分别以billNo和orderNo的形式传递到后台去处理，
	 * 模式一：new String[] {"billNo","orderNo:parentOrderNo"}意思是将parentOrderNo的值以orderNo的形式传递给后台去处理，也可以直接后台用orderNo得到全部也行
	 */
	public NodeUtils(BaseCrudService basCrudService,String[] linkFields) {
		this.basCrudService = basCrudService;
		this.linkFields = linkFields;
	}

	public BaseCrudService getBasCrudService() {
		return basCrudService;
	}

	public void setBasCrudService(BaseCrudService basCrudService) {
		this.basCrudService = basCrudService;
	}

	public String[] getLinkFields() {
		return linkFields;
	}

	public void setLinkFields(String[] linkFields) {
		this.linkFields = linkFields;
	}
	
	/**
	 * 得到所有节点，所有结果List将返回给resultMap的list
	 * @param resultMap 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getAllNodes(Map<String, Object> resultMap) throws Exception{
		if (resultMap==null){
			throw new Exception("节点的resultMap不能为空！");
		}
		if (linkFields==null){
			throw new Exception("取所有节点数据，必须指定linkFields关联字段！");
		}
		if (!(resultMap.get("list") instanceof ArrayList<?>)){
			throw new Exception("resultMap list类型有错误！");
		}
		
		List<ModeType> allList = new ArrayList<ModeType>();
		try {			
			List<ModeType> list = (List<ModeType>)resultMap.get("list");	

			//展开所有的层级的清单
			getAllNodes(allList, list, null);
			
			//重新处理分页(此种情况不考虑分页处理)
			if (resultMap.containsKey("totalCount")){
				resultMap.remove("totalCount");
			}
	        resultMap.put("list",allList); 
		} catch (Exception e) {
			throw e;
		}		
	}
	
	/**
	 * 得到所有节点，所有结果以List的形式返回
	 * @param resultMap 
	 * @throws Exception
	 */
	public List<ModeType> getAllNodes(List<ModeType> list) throws Exception{
		if (linkFields==null){
			throw new Exception("取所有节点数据，必须指定linkFields关联字段！");
		}
		List<ModeType> allList = new ArrayList<ModeType>();
		try {			
			//展开所有的层级的清单
			getAllNodes(allList, list, null);
			return  allList;
		} catch (Exception e) {
			throw e;
		}		
	}	
	
	private void getAllNodes(List<ModeType> allList, List<ModeType> list,Map<String, Object> params) throws Exception {
		for (ModeType model : list) {
			allList.add(model);
			Map<String, Object> nodeParams = new HashMap<String, Object>(1);
			
			Class<?> cls = model.getClass();
			for(Method method:cls.getMethods()){
				for(String field:linkFields){
					//parentCategoryId:categoryId(将categoryId传递给parentCategoryId，也可以在sql里灵活处理)
					String[] fields = field.split(":"); 
					if (method.getName().equalsIgnoreCase("get"+fields[fields.length-1])){
						nodeParams.put(fields[0], method.invoke(model));
					}
				}
			}
			List<ModeType> temp = basCrudService.findByBiz(model,nodeParams);
			getAllNodes(allList, temp, nodeParams);
		}
	}
}
