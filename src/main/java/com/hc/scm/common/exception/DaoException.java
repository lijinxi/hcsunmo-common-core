package com.hc.scm.common.exception;
import org.springframework.dao.DataAccessException;

/**
 * 
 * Description: 自定义Dao层异常信息
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-3下午2:23:28
 */
public class DaoException extends DataAccessException{

	    private static final long serialVersionUID = 1L;

	    public DaoException(String msg) {
	        super(msg);
	    }

	    public DaoException(String msg, Throwable cause) {
	        super(msg, cause);
	    }
	}