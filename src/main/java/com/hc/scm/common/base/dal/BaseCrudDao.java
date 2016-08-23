package com.hc.scm.common.base.dal;

import java.util.List;

import java.util.Map;

import com.hc.scm.common.enums.CommonOperatorEnum;
import com.hc.scm.common.exception.DaoException;
import com.hc.scm.common.utils.SimplePage;

/**
 * crud对应Service类
 * Description: 
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-3上午11:50:20
 */
public interface BaseCrudDao {
	public <ModelType> int deleteById(ModelType modelType) throws DaoException;
	
	public <ModelType> int add(ModelType modelType) throws DaoException;
	
	public <ModelType> ModelType findById(ModelType modelType)throws DaoException;
	/**
	 * 根据参数查询
	 * @param modelType 固定参数
	 * @param params 页面其他参数
	 * @return
	 * @throws DaoException
	 */
	public <ModelType> List<ModelType> findByBiz(ModelType modelType,Map<String,Object> params)throws DaoException;
	/**
	 * 根据id修改实体
	 * @param modelType
	 * @return
	 * @throws DaoException
	 */
	public <ModelType> int modifyById(ModelType modelType)throws DaoException;
	/**
	 * 
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public int findCount(Map<String,Object> params)throws DaoException;

	/**
	 * 根据参数查询列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public <ModelType> List<ModelType> findByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws DaoException;

	/**
	 * 公共的保存操作
	 * @param params key:增、删、改操作枚举 value:对象列表
	 * @return 影响条数
	 * @throws DaoException 
	 */
	public <ModelType> int save(Map<CommonOperatorEnum,List<ModelType>> params) throws DaoException;
	
	/**
	 * 根据参数删除
	 * @param record
	 * @return
	 * @throws DaoException
	 */
	public <ModelType> int deleteByPrimarayKeyForModel(ModelType record)throws DaoException;

}
