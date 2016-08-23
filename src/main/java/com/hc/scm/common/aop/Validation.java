package com.hc.scm.common.aop;

import org.aspectj.lang.JoinPoint;

import com.hc.scm.common.exception.ServiceException;
import com.hc.scm.common.exception.ValidException;
/**
 * Description: 公共验证接口类
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-12下午6:30:20
 */
public interface Validation {
	
	/**
	 * add前置验证接口
	 * @param jp
	 * @throws ServiceException
	 */
	public void doAddBefore(JoinPoint jp) throws ValidException;
	
	/**
	 * Delete前置验证接口
	 * @param jp
	 * @throws ServiceException
	 */
	public void doDeleteBefore(JoinPoint jp) throws ValidException;
	
	/**
	 * Save前置验证接口
	 * @param jp
	 * @throws ServiceException
	 */
	public void doSaveBefore(JoinPoint jp) throws ValidException;
}
