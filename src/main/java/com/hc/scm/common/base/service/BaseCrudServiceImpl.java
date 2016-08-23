package com.hc.scm.common.base.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hc.scm.common.base.dal.BaseCrudDao;
import com.hc.scm.common.base.dal.BaseSqlDao;
import com.hc.scm.common.constans.SysConstans;
import com.hc.scm.common.enums.CommonOperatorEnum;
import com.hc.scm.common.exception.ServiceException;
import com.hc.scm.common.model.CustomerRequest;
import com.hc.scm.common.model.ImportResolve;
import com.hc.scm.common.model.ImportValidationCondition;
import com.hc.scm.common.model.MasterRequest;
import com.hc.scm.common.model.SystemUser;
import com.hc.scm.common.utils.BillUtils;
import com.hc.scm.common.utils.CommonUtil;
import com.hc.scm.common.utils.SimplePage;
import com.hc.scm.common.utils.SizeHorizontalResolve;
import com.hc.scm.common.utils.SpringComponent;
import com.hc.scm.common.utils.ValidationBaseUtils;

public abstract class BaseCrudServiceImpl implements BaseCrudService{

	private BaseCrudDao baseCrudDao;
	
	
	@SuppressWarnings({ "unused" })
	@PostConstruct
	private void initConfig(){
		this.baseCrudDao=this.init();
		System.out.println("BaseCrudServiceImpl dao="+this.baseCrudDao);
	}
	
	public abstract BaseCrudDao init();
	
	@Override
	public <ModelType> int deleteById(ModelType modelType) throws ServiceException {
		try {
			return baseCrudDao.deleteById(modelType);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public <ModelType> int add(ModelType modelType) throws ServiceException {
		try {
			return baseCrudDao.add(modelType);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public <ModelType> ModelType findById(ModelType modelType) throws ServiceException {
		try {
			return baseCrudDao.findById(modelType);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public <ModelType> ModelType findByParams(Map<String,Object> params) throws ServiceException {
		try {
			List<ModelType> list=baseCrudDao.findByBiz(null, params);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public  Object findVoByParams(Map<String,Object> params) throws ServiceException {
		try {
			BaseSqlDao<Object> baseSqlDao=(BaseSqlDao<Object>) SpringComponent.getBean("baseSqlDao");
			return baseSqlDao.getVo(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	
	@Override
	public <ModelType> List<ModelType> findByBiz(ModelType modelType,
			Map<String, Object> params) throws ServiceException {
		try {
			return baseCrudDao.findByBiz(modelType, params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public <ModelType> int modifyById(ModelType modelType) throws ServiceException {
		try {
			return baseCrudDao.modifyById(modelType);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public int findCount(Map<String,Object> params) throws ServiceException {
		try {
			return baseCrudDao.findCount(params);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}

	@Override
	public <ModelType> List<ModelType> findByPage(SimplePage page, String orderByField,
			String orderBy,Map<String,Object> params) throws ServiceException {
		try {
			return baseCrudDao.findByPage(page, orderByField, orderBy, params);
		} catch (ServiceException e) {
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findVoByPage(SimplePage page, String orderByField,
			String orderBy,Map<String,Object> params) throws ServiceException {
		try {
			BaseSqlDao<Object> baseSqlDao=(BaseSqlDao<Object>) SpringComponent.getBean("baseSqlDao");
			return baseSqlDao.getListVoByPage(page, orderByField, orderBy, params);
		}
		catch (ServiceException e) {
			throw e;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=ServiceException.class)
	public <ModelType> int save(Map<CommonOperatorEnum, List<ModelType>> params,ModelType model,SystemUser systemUser,String billType) throws ServiceException {
		try {
			int count=0;
			for (Entry<CommonOperatorEnum, List<ModelType>> param : params.entrySet()) {
				if(param.getKey().equals(CommonOperatorEnum.DELETED)){
					List<ModelType> list=params.get(CommonOperatorEnum.DELETED);
					if(null!=list&&list.size()>0){
						for (ModelType modelType : list) {
							if(modelType instanceof JSONObject){
								count+=deleteById(JSON.parseObject(modelType.toString(), model.getClass()));
							}else{
								count+=deleteById(modelType);
							}
						}
					}
				}
				if(param.getKey().equals(CommonOperatorEnum.UPDATED)){
					List<ModelType> list=params.get(CommonOperatorEnum.UPDATED);
					if(null!=list&&list.size()>0){
						Object obj;
						Date date=new Date();
						for (ModelType modelType : list) {
							if(modelType instanceof JSONObject){
								obj=JSON.parseObject(modelType.toString(), model.getClass());
							}else{
								obj=modelType;
							}
							CommonUtil.setEntityDefaultField(obj, 1, systemUser,model.getClass(),date);//设置时间与编辑人
							count+=modifyById(obj);
						}
					}
				}
				if(param.getKey().equals(CommonOperatorEnum.INSERTED)){
					List<ModelType> list=params.get(CommonOperatorEnum.INSERTED);
					if(null!=list&&list.size()>0){
						Object obj;
						Date date=new Date();
						String objJson;
						for (ModelType modelType : list) {
							if(modelType instanceof JSONObject){
								objJson=modelType.toString();
								obj=JSON.parseObject(modelType.toString(), model.getClass());
							}else{
								obj=modelType;
								objJson=JSON.toJSONString(modelType);
							}
							if(StringUtils.isNotBlank(billType)){//生成单据
								String billNo=BillUtils.getBillNo(billType, objJson);
								BillUtils.setBillNoAndCreator(obj, model.getClass(), systemUser, date,billNo);
							}else{
								CommonUtil.setEntityDefaultField(obj, 0, systemUser, model.getClass(), date);
							}
							add(obj);
						}
						count+=list.size();
					}
				}
			}
			return count;
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
	}
	

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public <ModelType,ModelCustomerType> void addMasterCustomer(ModelType modelType,
			List<ModelCustomerType> listModelCustomerType,String idField,String customerName,SystemUser systemUser){
		Date date=new Date();
		CommonUtil.setEntityDefaultField(modelType, 0, systemUser, modelType.getClass(),date);
		baseCrudDao.add(modelType);
		Object masterId;
		customerName=CommonUtil.changeFirstCharUporLow(customerName, 1);
		String customerNameDao=customerName+"Dao";
		masterId=CommonUtil.getFieldValue(modelType, idField, modelType.getClass());
		BaseCrudDao baseCrudDaoCustomer=(BaseCrudDao)SpringComponent.getBean(customerNameDao);
		for(ModelCustomerType molC:listModelCustomerType){
			CommonUtil.setEntityDefaultField(molC, 0, systemUser, molC.getClass(),date);
			CommonUtil.setFieldValue(molC, idField, molC.getClass(), masterId);
			baseCrudDaoCustomer.add(molC);
		}
	}
	
	@SuppressWarnings({ "static-access","rawtypes" })
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public <ModelType> Object	saveMasterCustomerList(ModelType modelType,List<CustomerRequest> listData
			,SystemUser systemUser,String modelClassName,MasterRequest masterRequest) throws Exception{
		Date date=new Date();
		Object masterId=saveMasterTable(modelType, masterRequest, systemUser,  date);//保存主表
		if(listData==null)return masterId;
		Class<?> customerClazz;
		String masterNameAllPath;
		BaseCrudService baseCrudDaoCustomer;
		List listCustomerInsert=new ArrayList<Object>();
		List listCustomerUpdate=new ArrayList<Object>();
		List listCustomerDelete=new ArrayList<Object>();
		for(CustomerRequest mcr:listData){//遍历不同从表
			masterNameAllPath=modelClassName+"."+CommonUtil.changeFirstCharUporLow(mcr.getCustomerName(), 0);
			customerClazz= this.getClass().getClassLoader().getClass().forName(masterNameAllPath);//获得从表对象
			String customerNameDao=CommonUtil.changeFirstCharUporLow(mcr.getCustomerName(), 1)+"Service";
			baseCrudDaoCustomer=(BaseCrudService)SpringComponent.getBean(customerNameDao);
			saveCustomerTable(baseCrudDaoCustomer, listCustomerInsert, listCustomerUpdate,//保存从表
					listCustomerDelete, customerClazz, masterId, masterRequest, date, systemUser, mcr);
		}
		return masterId;
	}
	
	
	@Override
	public <ModelType>  void audit(List<ModelType> auditModelList ,SystemUser systemUser){
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public <ModelType> void importExcel(ImportResolve<ModelType> importResolve,List<ImportValidationCondition> listCondition,
			SystemUser systemUser,String isValidateAll)  throws ServiceException{
		StringBuffer sb=new StringBuffer();
		Date date=new Date();
		int index=0;
		if(isValidateAll.equals("N")){//验证成功的导入(部分成功)
			StringBuffer succesSb=new StringBuffer("");
			for(ModelType modelType: importResolve.getDataList() ){
				String ret= ValidationBaseUtils.validationImport(modelType, listCondition);
				if(ret.length()==0){
					CommonUtil.setEntityDefaultField(modelType, 0, systemUser,  modelType.getClass(), date);
					add(modelType);
					succesSb.append(" 第").append(importResolve.getRowlist().get(index)).append("行").append("<br/>");
				}else{
					sb.append("【 第").append(importResolve.getRowlist().get(index)).append("行】:").append(ret).append("<br/>");
				}
				index++;
			  }
			importResolve.setSuccesMsg(succesSb.toString());
			importResolve.setErrorMsg(importResolve.getErrorMsg()+sb.toString());
		}else{//全部验证成功才导入
			index=0;
			for(ModelType modelType: importResolve.getDataList() ){
				String ret= ValidationBaseUtils.validationImport(modelType, listCondition);
				if(ret.length()>0){
					sb.append("【第").append(importResolve.getRowlist().get(index)).append("行】:").append(ret).append("<br/>");
				}
				index++;
			}
			if(sb.length()>0){
				importResolve.setErrorMsg(importResolve.getErrorMsg()+sb.toString());
				return;
			}else{
				for(ModelType modelType: importResolve.getDataList() ){
					CommonUtil.setEntityDefaultField(modelType, 0, systemUser,  modelType.getClass(), date);
					add(modelType);
				}
			}
		}
	}
	
	/**
	 * 尺码横排保存
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public  <ModelType> Object  saveSizeHorizontal(ModelType modelType,MasterRequest masterRequest,
			String modelClassName,SystemUser systemUser) throws Exception{
		Date date=new Date();
		Object masterId=saveMasterTable(modelType, masterRequest, systemUser,date);
		if(StringUtils.isBlank(masterRequest.getCustomerListData()))return masterId;
		Class<?> customerClazz;
		String masterNameAllPath;
		BaseCrudService baseCrudDaoCustomer;
		List<Object> listCustomerInsert=new ArrayList<Object>();
		List<Object> listCustomerUpdate=new ArrayList<Object>();
		@SuppressWarnings("rawtypes")
		List listCustomerDel=new ArrayList<Object>();
		@SuppressWarnings("rawtypes")
		HashMap<String, Map> sizeTypeMap=new HashMap<String, Map>();
		List<CustomerRequest> listData=JSON.parseArray(masterRequest.getCustomerListData(), CustomerRequest.class);
		 for(CustomerRequest mcr:listData){//遍历不同从表
				masterNameAllPath=modelClassName+"."+CommonUtil.changeFirstCharUporLow(mcr.getCustomerName(), 0);
				customerClazz= Class.forName(masterNameAllPath);//获得从表对象
				String customerNameDao=CommonUtil.changeFirstCharUporLow(mcr.getCustomerName(), 1)+"Service";
				baseCrudDaoCustomer=(BaseCrudService)SpringComponent.getBean(customerNameDao);
				if(StringUtils.isNotBlank(mcr.getIsSizeHorizontal()) && mcr.getIsSizeHorizontal().equals("1")){//保存尺码横排数据
					if(StringUtils.isNotBlank(mcr.getInsertlist())){
						List<String> detailList=JSON.parseArray(mcr.getInsertlist(), String.class);
						for(String detail:detailList){
							listCustomerInsert.addAll(SizeHorizontalResolve.resolveSizeHorizontalFromJson(detail, customerClazz,sizeTypeMap,mcr.getType(),mcr.getQtyProperty()));
						}
						for(Object cus:listCustomerInsert){
						    if( masterRequest.getOperateType().equals(SysConstans.INSERTED_TYPE)){//主表新增才设置主键，主表不是新增从表关联主表id从前台设
								CommonUtil.setFieldValue(cus, masterRequest.getIdFieldName(), customerClazz, masterId);//设置主表的主键
							}
							baseCrudDaoCustomer.add(cus);
					   }
					}else if(StringUtils.isNotBlank(mcr.getUpdatelist())){
						List<String> detailList=JSON.parseArray(mcr.getUpdatelist(), String.class);
						for(String detail:detailList){
							listCustomerUpdate.addAll(SizeHorizontalResolve.resolveSizeHorizontalFromJson(detail, customerClazz,sizeTypeMap,mcr.getType(),mcr.getQtyProperty()));
							baseCrudDaoCustomer.deleteById(JSON.parseObject(detail, customerClazz));
						}
						for(Object cus:listCustomerUpdate){
						    if( masterRequest.getOperateType().equals(SysConstans.INSERTED_TYPE)){//主表新增才设置主键，主表不是新增从表关联主表id从前台设
								CommonUtil.setFieldValue(cus, masterRequest.getIdFieldName(), customerClazz, masterId);//设置主表的主键
							}
							baseCrudDaoCustomer.add(cus);
					   }
					}
					else if(StringUtils.isNotBlank(mcr.getDeletelist())){
						listCustomerDel= JSON.parseArray(mcr.getDeletelist(), customerClazz);//解析从表数据
						for(Object cus:listCustomerDel){
							baseCrudDaoCustomer.deleteById(cus);
						}
					}
				}else{//保存非尺码横排数据
					saveCustomerTable(baseCrudDaoCustomer, listCustomerInsert, listCustomerUpdate,
							listCustomerDel, customerClazz, masterId, masterRequest, date, systemUser, mcr);
				}
			}
		return masterId;
	}
	
	/**
	 * 保存从表
	 */
	@SuppressWarnings("rawtypes")
	private void saveCustomerTable(BaseCrudService baseCrudDaoCustomer, List listCustomerInsert,
			List listCustomerUpdate,List listCustomerDelete,Class<?> customerClazz
			,Object masterId,MasterRequest masterRequest,Date date,SystemUser systemUser,CustomerRequest mcr){

		if(StringUtils.isNotBlank(mcr.getInsertlist())){////从表新增
			listCustomerInsert=JSON.parseArray(mcr.getInsertlist(), customerClazz);//解析从表数据
			for(Object cus:listCustomerInsert){
				CommonUtil.setEntityDefaultField(cus, 0, systemUser,customerClazz,date);//设置时间与编辑人
				if( masterRequest.getOperateType().equals(SysConstans.INSERTED_TYPE)){//主表新增才设置主键，主表不是新增从表关联主表id从前台设
					CommonUtil.setFieldValue(cus, masterRequest.getIdFieldName(), customerClazz, masterId);//设置主表的主键
				}
				baseCrudDaoCustomer.add(cus);
			}
		}
		if(StringUtils.isNotBlank(mcr.getUpdatelist())){//从表修改
			listCustomerUpdate= JSON.parseArray(mcr.getUpdatelist(), customerClazz);//解析从表数据
			for(Object cus:listCustomerUpdate){
				CommonUtil.setEntityDefaultField(cus, 1, systemUser,customerClazz,date);//设置时间与编辑人
				baseCrudDaoCustomer.modifyById(cus);
			}
		}
		if(StringUtils.isNotBlank(mcr.getDeletelist())){///从表删除
			listCustomerDelete= JSON.parseArray(mcr.getDeletelist(), customerClazz);//解析从表数据
				for(Object cus:listCustomerDelete){
					baseCrudDaoCustomer.deleteById(cus);
				}
			
		}
	}
	
	
	/**
	 * 保存主从的主表
	 * @param modelType
	 * @param masterRequest
	 * @param systemUser
	 * @param masterId
	 * @param date
	 * @throws Exception
	 */
	protected <ModelType> Object saveMasterTable(ModelType modelType,MasterRequest masterRequest,SystemUser systemUser,Date date ) throws Exception{
		Object masterId = null;
		if(StringUtils.isNotBlank(masterRequest.getOperateType()) &&modelType!=null){
			if( masterRequest.getOperateType().equals(SysConstans.INSERTED_TYPE)){//主表新增
				if(StringUtils.isNotBlank(masterRequest.getBillType())){//生成单据
					String billNo= BillUtils.getBillNo(masterRequest.getBillType(), masterRequest.getMasterJson());
					if(StringUtils.isBlank(billNo))throw new Exception("生成单据号失败");
					BillUtils.setBillNoAndCreator(modelType, modelType.getClass(), systemUser, date,billNo);
				}else{
					CommonUtil.setEntityDefaultField(modelType, 0, systemUser, modelType.getClass(), date);
				}
				add(modelType);
				masterId=CommonUtil.getFieldValue(modelType, masterRequest.getIdFieldName(), modelType.getClass());//获得主表主键的值
			}else if(masterRequest.getOperateType().equals(SysConstans.UPDATED_TYPE)){//主表修改
				CommonUtil.setEntityDefaultField(modelType, 1, systemUser, modelType.getClass(),date);
				modifyById(modelType);
			}else if(masterRequest.getOperateType().equals(SysConstans.DELETED_TYPE)){//主表删除
				deleteById(modelType);
			}
		}
		return masterId;
	}
	
}