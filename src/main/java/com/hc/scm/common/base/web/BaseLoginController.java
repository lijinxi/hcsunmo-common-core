package com.hc.scm.common.base.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hc.scm.common.base.service.BaseCrudService;
import com.hc.scm.common.constans.SysConstans;
import com.hc.scm.common.constans.WebVersionConfig;
import com.hc.scm.common.model.SystemUser;
import com.hc.scm.common.shiro.ShiroSession;
import com.hc.scm.common.utils.CommonUtil;
import com.hc.scm.common.utils.CookieUtils;
import com.hc.scm.common.utils.ResultModel;
import com.hc.scm.common.utils.SessionUtils;
import com.hc.scm.common.utils.SpringComponent;

/**
 * Description: 公共controller
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-3下午12:37:23
 */
public abstract class BaseLoginController<ModelType> {
	private static Logger logger = LoggerFactory.getLogger(BaseLoginController.class);
	private BaseCrudService baseService;
	
	@Value("#{configProperties['shiro.logoutUrl']}")
	private String shiroLogoutUrl;
	
	@Value("#{configProperties['session.dev.defaultLoginUser']}")
	private String defaultLoginUser; //开发环境系统默认登录用户
	@Resource
	private ShiroSession ShiroSession;
	
	@PostConstruct
	protected void initConfig() {
		System.out.println("军情解码---1");
		this.baseService = this.init();
	}

	protected abstract BaseCrudService init();
	
	@RequestMapping("/login.json")
    @ResponseBody
    public Map<String,Object> login_json(String loginName, String loginPassword,String cookieFlag, HttpServletRequest req, HttpServletResponse response){
		Map<String, Object> resultMap =new HashMap<String, Object>();
		ResultModel resultModel = new ResultModel();
		System.out.println("你们好，这是我最后的晚餐");
		try {
			if (StringUtils.isEmpty(loginName) ||StringUtils.isEmpty(loginPassword)) {
				resultModel.setResultCode("9003");
				resultModel.setMsg("用户名或密码不能为空");
            }
			
			
			if ("0".equals(resultModel.getResultCode())) {
				//验证用户名和密码
				SystemUser loginUser = new SystemUser();
				String passWord = CommonUtil.md5(loginPassword); //md5加密  
				loginUser.setUserCode(loginName);
				//loginUser.setPassword(passWord);
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("userCode", loginName);
				String userJson=JSON.toJSONString(baseService.findByParams(params));//数据库取用户信息
				loginUser=(SystemUser) JSON.parseObject(userJson,SystemUser.class);
				if (loginUser.getPassword().equals(passWord)) {
					//登录成功
					req.getSession().setAttribute(SysConstans.SESSION_USER, loginUser);
					SessionUtils.set(SysConstans.SESSION_USER, loginUser);
					
					//设置cookie保存登录信息
					if (StringUtils.isNotEmpty(cookieFlag) && cookieFlag.equals("1")) {
						CookieUtils.addCookie(req, response, SysConstans.LOGIN_SYSTEM_USER_COOKIE_ID,
								loginUser.getUserCode(), SysConstans.LOGIN_COOKIE_TIME);
					}
					resultModel.setMsg("登录成功!");

				} else {
					resultModel.setResultCode("9003");
					resultModel.setMsg("用户名或密码错误!");
				}
			}
		} catch (Exception e) {
			resultModel.setResultCode("9009");
			resultModel.setMsg("系统异常");
			logger.error("error:", e);
		}
		resultMap.put("result", resultModel);
        return resultMap;
    }
	
	/**
	 * 注销
	 * @throws Exception 
	 */
	@RequestMapping("/logout.json")
	@ResponseBody
	public Map<String,Object> logout_json(HttpServletRequest req,HttpServletResponse response, Model model) {
		Map<String, Object> resultMap =new HashMap<String, Object>();
		System.out.println("你们好，这是我最后的晚餐2");
		ResultModel resultModel = new ResultModel();
		try {
			logoutMethod(req, response);
			//退出登录，跳转首页
			response.sendRedirect(shiroLogoutUrl);
		} catch (Exception e) {
			resultModel.setResultCode("9009");
			resultModel.setMsg("系统异常");
			logger.error("error:", e);
		}
		resultMap.put("result", resultModel);
        return resultMap;
	}

	private void logoutMethod(HttpServletRequest request, HttpServletResponse response) {
		try {
			System.out.println("你们好，这是我最后的晚餐1");
			HttpSession session = request.getSession(false);
			if (session != null) {
				//清除登录用户session
				session.removeAttribute(SysConstans.SESSION_USER);
			}
			
			String userCode=request.getRemoteUser();
	    	if(StringUtils.isNotEmpty(userCode)){
	    		//清除cas的登录缓存
	    		ShiroSession shiroSession=(ShiroSession) SpringComponent.getBean("ShiroSession");
				shiroSession.removeMointorRealmCache(userCode);
				//清除登录要输入验证的标志
				CookieUtils.cancleCookie(response, "cookie.user.validcode.input", null);
	    	}
	    	
	    	Subject subjects = SecurityUtils.getSubject();
			if(subjects!=null){
				subjects.logout();
			}
		} catch (Exception e) {
			logger.error("logoutMethod error:", e);
		}
		
	}
	
    /**
	 * 获得当前用户信息
	 * @throws Exception 
	 */
	@RequestMapping("/getCurrentLoginUser.json")
	@ResponseBody
	public Map<String,Object> getCurrentLoginUser(HttpServletRequest req,HttpServletResponse response, Model model) {
		System.out.println("你们好，这是我最后的晚餐--------------********");
		Map<String, Object> resultMap =new HashMap<String, Object>();
		System.out.println("你们好，这是我最后的晚餐---------------**********3");
		ResultModel resultModel = new ResultModel();
		SystemUser user=null;
		
		try {
			if(req.getSession().getAttribute(SysConstans.SESSION_USER)!=null){
				System.out.println("你们好，这是我最后的晚餐---------------**********7");
				user=(SystemUser)req.getSession().getAttribute(SysConstans.SESSION_USER);
				user.setPassword("");
				user.setUserMenuMap(null);
				resultMap.put("result", resultModel);
				resultMap.put("entity", user);
			}
			else if(StringUtils.isNotEmpty(defaultLoginUser)){
				System.out.println("你们好，这是我最后的晚餐---------------**********31");
				/*Map<String,Object> params=new HashMap<String,Object>();
				params.put("userCode", defaultLoginUser);
				String userJson=JSON.toJSONString(baseService.findByParams(params));//数据库取用户信息
				user=(SystemUser) JSON.parseObject(userJson,SystemUser.class);*/
				user=ShiroSession.getUserByLoginNamePassword(defaultLoginUser,null);
				user.setPassword("");
				user.setUserMenuMap(null);
				resultMap.put("result", resultModel);
				resultMap.put("entity", user);
			}
			else{
				resultMap=getLoginOutResult();
			}
			
			//返回当前系统运行的环境
			resultMap.put("env", getEnvParam());
			//返回web项目的版本号
			WebVersionConfig config = (WebVersionConfig)SpringComponent.getBean("webVersionConfig");
			resultMap.put("version", config.getVersion());
			
		} catch (Exception e) {
			resultModel.setResultCode("9009");
			resultModel.setMsg("系统异常");
			logger.error("error:", e);
		}
		System.out.println("你们好，这是我最后的晚餐---------------**********3---"+resultMap.size());
        return resultMap;
	}
	
	/**
	 * 获得应用list
	 * @throws Exception 
	 */
	@RequestMapping("/getAppUrlList.json")
	@ResponseBody
	public Map<String,Object> getAppUrlList(HttpServletRequest req,HttpServletResponse response, Model model) {
		Map<String, Object> resultMap =new HashMap<String, Object>();
		System.out.println("你们好，这是我最后的晚餐3");
		ResultModel resultModel = new ResultModel();
		SystemUser user=null;
		try {
			String userCode=req.getRemoteUser();
	    	if(StringUtils.isNotEmpty(userCode)){
	    		user=ShiroSession.getUserByLoginNamePassword(defaultLoginUser,null);
	    		if(user!=null){
	    			resultMap.put("appUrlList", user.getAppUrlList());
	    		}
	    		else{
	    			resultModel.setResultCode("-1");
				}
	    	}
	    	else{
    			resultModel.setResultCode("-1");
			}
		} catch (Exception e) {
			resultModel.setResultCode("9009");
			resultModel.setMsg("系统异常");
			logger.error("error:", e);
		}
		resultMap.put("result", resultModel);
		
        return resultMap;
	}
	
	
	/**
     * 返回TimeOut
     * @return
     */
    private Map<String,Object> getLoginOutResult(){
    	Map<String, Object> resultMap =new HashMap<String, Object>();
    	ResultModel resultModel = new ResultModel();
    	resultModel.setResultCode("timeout");
    	resultModel.setMsg("登录超时，请重新登录。");
    	resultMap.put("result", resultModel);
		return resultMap;
    }
    
    /**
     * 获取系统环境
     * @return
     */
    private String getEnvParam(){
    	String envStr = "";
    	String env=System.getProperty("env");
		if("experience".equalsIgnoreCase(env)){
			envStr = "（体验环境）";
		}else if("dev".equalsIgnoreCase(env)){
			envStr = "（开发环境）";
		}else if("st".equalsIgnoreCase(env)){
			envStr = "（ST测试环境）";
		}else if("test".equalsIgnoreCase(env)){
			envStr = "（测试环境）";
		}else if("online".equalsIgnoreCase(env)){
			envStr = "（生产环境）";
		}
		
		return envStr;
    }
}