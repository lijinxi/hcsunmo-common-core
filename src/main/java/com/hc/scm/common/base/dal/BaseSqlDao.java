package com.hc.scm.common.base.dal;

import java.util.List;

import java.util.Map;

import com.hc.scm.common.exception.DaoException;
import com.hc.scm.common.utils.SimplePage;



/**
 *VO BaseSqlDao类
 * @param <T>
 */

public interface BaseSqlDao<T> {
	/**
	 * 查询VO对象
	 * @param params
	 * @return
	 * @throws DaoException
	 */
	public T getVo(Map<String,Object> params) throws DaoException;
	
	/**
	 * 查询ListVO对象
	 * @param page
	 * @param orderByField
	 * @param orderBy
	 * @param params
	 * @return
	 * @throws DaoException
	 */
    public List<T> getListVoByPage(SimplePage page,String orderByField,String orderBy,Map<String,Object> params) throws DaoException;
}