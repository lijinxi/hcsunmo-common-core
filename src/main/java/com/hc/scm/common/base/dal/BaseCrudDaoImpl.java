package com.hc.scm.common.base.dal;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hc.scm.common.base.mapper.BaseCrudMapper;
import com.hc.scm.common.enums.CommonOperatorEnum;
import com.hc.scm.common.exception.DaoException;
import com.hc.scm.common.utils.SimplePage;

public abstract class BaseCrudDaoImpl implements BaseCrudDao{

	
	private BaseCrudMapper mapper;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void initConfig(){
		this.mapper=this.init();
	}
	
	public abstract BaseCrudMapper init();

	
	@Override
	public <ModelType> int deleteById(ModelType modelType) throws DaoException {
		try {
			return mapper.deleteByPrimarayKeyForModel(modelType);
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}

	@Override
	public <ModelType> int add(ModelType modelType) throws DaoException {
		try {
			return mapper.insertSelective(modelType);
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}

	@Override
	public <ModelType> ModelType findById(ModelType modelType) throws DaoException {
		try {
			return mapper.selectByPrimaryKey(modelType);
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}
	
	@Override
	public <ModelType> List<ModelType> findByBiz(ModelType modelType,
			Map<String, Object> params) throws DaoException {
		try {
			return mapper.selectByParams(modelType, params);
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}

	@Override
	public <ModelType> int modifyById(ModelType modelType) throws DaoException {
		try {
			return mapper.updateByPrimaryKeySelective(modelType);
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}

	@Override
	public int findCount(Map<String,Object> params) throws DaoException {
		try {
			return mapper.selectCount(params);
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}

	@Override
	public <ModelType> List<ModelType> findByPage(SimplePage page, String orderByField,
			String orderBy,Map<String,Object> params) throws DaoException {
		try {
			return mapper.selectByPage(page, orderByField, orderBy, params);
		} catch (DaoException e) {
			throw e;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=DaoException.class)
	public <ModelType> int save(Map<CommonOperatorEnum, List<ModelType>> params) throws DaoException {
		try {
			int count=0;
			for (Entry<CommonOperatorEnum, List<ModelType>> param : params.entrySet()) {
				if(param.getKey().equals(CommonOperatorEnum.DELETED)){
					List<ModelType> list=params.get(CommonOperatorEnum.DELETED);
					if(null!=list&&list.size()>0){
						for (ModelType modelType : list) {
							count+=this.mapper.deleteByPrimarayKeyForModel(modelType);
						}
					}
				}
				if(param.getKey().equals(CommonOperatorEnum.UPDATED)){
					List<ModelType> list=params.get(CommonOperatorEnum.UPDATED);
					if(null!=list&&list.size()>0){
						for (ModelType modelType : list) {
							count+=this.mapper.updateByPrimaryKeySelective(modelType);
						}
					}
				}
				if(param.getKey().equals(CommonOperatorEnum.INSERTED)){
					List<ModelType> list=params.get(CommonOperatorEnum.INSERTED);
					if(null!=list&&list.size()>0){
						for (ModelType modelType : list) {
							this.mapper.insertSelective(modelType);
						}
						count+=list.size();
					}
				}
			}
			return count;
		} catch (Exception e) {
			throw new DaoException("",e);
		}
	}
	
	@Override
	public <ModelType> int deleteByPrimarayKeyForModel(ModelType record)throws DaoException{
		return mapper.deleteByPrimarayKeyForModel(record);
	}
	
}