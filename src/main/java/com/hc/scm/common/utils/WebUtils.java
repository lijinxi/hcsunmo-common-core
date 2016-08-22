package com.hc.scm.common.utils;

import javax.servlet.http.HttpServletRequest;

import com.hc.scm.common.constans.SysConstans;
import com.hc.scm.common.model.SystemUser;

public class WebUtils {
	/**
	 * 获取当前登录用户 若当前登录用户为空 则默认admin 后期需进行屏蔽
	 * @param req
	 * @return
	 */
	public static SystemUser getCurrentLoginUser(HttpServletRequest req){
		SystemUser currentUser = null;
		if(req.getSession().getAttribute(SysConstans.SESSION_USER)!=null){
			currentUser=(SystemUser)req.getSession().getAttribute(SysConstans.SESSION_USER);
		}
		//currentUser = currentUser==null ? new SystemUser(1, "admin", "超级管理员") : currentUser;
		return currentUser;
	}
}
