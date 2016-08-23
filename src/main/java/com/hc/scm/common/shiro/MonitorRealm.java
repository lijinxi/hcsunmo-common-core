package com.hc.scm.common.shiro;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
/**
 * 
 * Description: 角色权限认证
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-5-12下午2:23:13
 */
public class MonitorRealm extends AuthorizingRealm {
	protected Logger logger = LoggerFactory.getLogger(MonitorRealm.class);
	@Resource
	private ShiroSession ShiroSession;
	
    public MonitorRealm() {  
        super();  
    }  
  
    /**
     * 授权代码(默认admin)
     */
    @Override  
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {  
    	String username = (String) principals.getPrimaryPrincipal(); 
    	if (logger.isDebugEnabled()){
    		logger.debug("doGetAuthorizationInfo() username:"+username);
    	}
		 Subject subject = SecurityUtils.getSubject();
		 if(subject!=null){
			 if (StringUtils.isNotEmpty(username)) {
					SimpleAuthorizationInfo info =ShiroSession.getUserRolepermission(username);
					return info;
				} else {
					return null;
				}
		 }
		 else{
			 return null;
		 }
    }  
    
    /**
     * 登录认证
     */
    @Override  
    protected AuthenticationInfo doGetAuthenticationInfo(  
            AuthenticationToken authcToken) throws AuthenticationException {  
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;  
        String userName = token.getUsername();  
        if (logger.isDebugEnabled()){
        	logger.debug("doGetAuthenticationInfo() userName:"+userName+" token.getPassword():"+String.valueOf(token.getPassword()));
        }
		try {
			/*if(StringUtils.isNotEmpty(userName)){
				SystemUser user =null;
				Session session = getSession();
	            if(session.getAttribute(SysConstans.SESSION_USER)==null){
	            	user =ShiroSession.getUserByLoginNamePassword(userName, String.valueOf(token.getPassword()));
	            	if(user!=null){
	            		session.setAttribute(SysConstans.SESSION_USER, user);
	            		return new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), getName());  
	            	}
	            }
	            else{
	            	user=(SystemUser) session.getAttribute(SysConstans.SESSION_USER);
	            }
			}*/
			
			if(token!=null){
				return new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), getName()); 
			}
		} catch (Exception e) {
			logger.error("doGetAuthenticationInfo error:",e);
		}
		//clearCachedAuthorizationInfo(token.getUsername());
        return null;  
    }  
    
    /**
	 * 获取当前的session
	 * @return
	 */
	public Session getSession(){
		Subject currentUser = SecurityUtils.getSubject();  
        if(null != currentUser){  
        	Session session=currentUser.getSession();
            return  session;
        }
        return null;
	}
  
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());  
        clearCachedAuthorizationInfo(principals);  
    }
    
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
      super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
      super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
      super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
      getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
      getAuthenticationCache().clear();
    }

    public void clearAllCache() {
      clearAllCachedAuthenticationInfo();
      clearAllCachedAuthorizationInfo();
    }
    
}  