package com.hc.scm.common.base.service;

import java.util.List;
import java.util.Map;

import com.hc.scm.common.enums.CommonOperatorEnum;
import com.hc.scm.common.exception.ServiceException;
import com.hc.scm.common.model.CustomerRequest;
import com.hc.scm.common.model.ImportResolve;
import com.hc.scm.common.model.ImportValidationCondition;
import com.hc.scm.common.model.MasterRequest;
import com.hc.scm.common.model.SystemUser;
import com.hc.scm.common.utils.SimplePage;

/**
 * crud对应Service类
 * Description: 
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-3上午11:50:20
 */
public interface BaseCrudService {
	public <ModelType> int deleteById(ModelType modelType) throws ServiceException;
	
	public <ModelType> int add(ModelType modelType) throws ServiceException;
	
	public <ModelType> ModelType findById(ModelType modelType)throws ServiceException;
	
	/**
	 * 根据参数查询
	 * @param params 页面其他参数
	 * @return
	 * @throws ServiceException
	 */
	public <ModelType> ModelType findByParams(Map<String,Object> params)throws ServiceException;
	
	/**
	 * 根据参数查询
	 * @param modelType 固定参数
	 * @param params 页面其他参数
	 * @return
	 * @throws ServiceException
	 */
	public <ModelType> List<ModelType> findByBiz(ModelType modelType,Map<String,Object> params)throws ServiceException;
	/**
	 * 根据id修改实体
	 * @param modelType
	 * @return
	 * @throws ServiceException
	 */
	public <ModelType> int modifyById(ModelType modelType)throws ServiceException;
	/**
	 * 
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public int findCount(Map<String,Object> params)throws ServiceException;

	/**
	 * 根据参数查询VO
	 * @param params 页面其他参数
	 * @return
	 * @throws ServiceException
	 */
	public Object findVoByParams(Map<String,Object> params)throws ServiceException;
	
	/**
	 * 根据参数查询VO列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public List<Object> findVoByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ServiceException;
	
	/**
	 * 根据参数查询列表
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	public <ModelType> List<ModelType> findByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params)throws ServiceException;

	/**
	 * 公共的保存操作
	 * @param params key:增、删、改操作枚举 value:对象列表
	 * @return 影响条数
	 * @throws ServiceException 
	 */
	public <ModelType> int save(Map<CommonOperatorEnum,List<ModelType>> params,ModelType modelType,SystemUser systemUser,String billType) throws ServiceException;
	
	/**
	 * 主从表新增一主对一从）
	 * @param reqModel
	 */
	public <ModelType,ModelCustomerType> void addMasterCustomer(ModelType modelType,List<ModelCustomerType> 
	listModelCustomerType,String idField,String customerName,SystemUser systemUser);
	
	
	/**
	 * 主从表新增(一主对多从）
	 *返回主键值
	 * @param reqModel
	 */
	public <ModelType> Object saveMasterCustomerList(ModelType modelType,List<CustomerRequest> listData
	,SystemUser systemUser,String modelClassName,MasterRequest masterRequest)throws Exception;
	
	/**
	 * 审核单据
	 * @param auditModelList
	 * @param systemUser
	 */
	public <ModelType>  void audit(List<ModelType> auditModelList ,SystemUser systemUser);

	/**
	 * 导入
	 * @param list
	 * @param listCondition
	 * @throws ServiceException
	 */
	public <ModelType> void importExcel(ImportResolve<ModelType> importResolve,
			List<ImportValidationCondition> listCondition,SystemUser systemUser,String isValidateAll)  throws ServiceException;
	
	/**
	 * 保存尺码横排
	 * @param modelType
	 * @param sizeHorizontalReq
	 * @param modelClassName
	 * @param systemUser
	 * @return
	 */
	public  <ModelType> Object  saveSizeHorizontal(ModelType modelType,MasterRequest masterRequest ,String modelClassName,SystemUser systemUser) throws Exception;
}
