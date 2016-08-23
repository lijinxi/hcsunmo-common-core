package com.hc.scm.common.base.dal;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.hc.scm.common.exception.DaoException;
import com.hc.scm.common.utils.SimplePage;

 

/**
 * VO 公共数据库操作dao
 * @author wugy
 * @version 1.0
 */
public class BaseSqlDaoImpl<T> implements BaseSqlDao<T> {
	
	/**
	 * vo(model)包路径
	 */
	private String baseVoPackage;
	
	/**
	 *Mapper包路径
	 */
	private String baseMapperPackage;
	
	public String getBaseVoPackage() {
		return baseVoPackage;
	}

	public void setBaseVoPackage(String baseVoPackage) {
		this.baseVoPackage = baseVoPackage;
	}
	

	public String getBaseMapperPackage() {
		return baseMapperPackage;
	}

	public void setBaseMapperPackage(String baseMapperPackage) {
		this.baseMapperPackage = baseMapperPackage;
	}
	
	@Resource
	private SqlSessionFactory sqlSessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public T getVo(Map<String,Object> params) throws DaoException {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			String selectVoName=params.get("selectVoName")==null? "baseSelectOneModelByVo":params.get("selectVoName").toString();
			Object obj=session.selectOne(getBaseMapperPackage()+"."+params.get("mapperClassType")+"."+selectVoName,params);
			return (T) obj;
		} finally {
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getListVoByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params) throws DaoException {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			String selectVoName=params.get("selectVoName")==null? "baseSelectListByVo":params.get("selectVoName").toString();
			if(StringUtils.isNotEmpty(orderByField)){
				params.put("orderByField", orderByField);
			}
			if(StringUtils.isNotEmpty(orderBy)){
				params.put("orderBy", orderBy);
			}
			List<Map<String,Object>> list=session.selectList(getBaseMapperPackage()+"."+params.get("mapperClassType")+"."+selectVoName,params);
			return (List<T>) list;
		}
		finally {
			session.close();
		}
	}


}
