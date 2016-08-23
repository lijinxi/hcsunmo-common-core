package com.hc.scm.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hc.scm.common.model.SystemUser;

/**
 * 登录操作工具类
 * Description: 
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-25上午10:07:57
 */
public class LoginUtils {
	private static Logger logger = LoggerFactory.getLogger(LoginUtils.class);
	
	/*
	 * 不拦截的url
	 */
	private static List<String> excludeUrlsList=new ArrayList<String>();
	
	/*
	 * 按钮操作权限
	 */
	static HashMap<String, Integer> rmap=new HashMap<String, Integer>();
	static{
		excludeUrlsList.add("/hcc-web/getCurrentLoginUser.json");
		excludeUrlsList.add("/hchcweb/itg_menu_list/getusermenulist.json");
		excludeUrlsList.add("/hc-uhcb/logout.json");
		excludeUrlsList.add("/hc-uc-hcclearUserCache.json");
		
		
		/**
		 * 模块的right_value大于等于下列值则拥有相应操作权限
		 * 1 浏览
		 * 2 编辑
		 * 4 增加
		 * 8 删除
		 * 16 打印设置
		 * 32 打印
		 * 64 导出
		 * 128 赋权
		 * 256 权限传递
		 */
		rmap.put("/add.json", 4);
		rmap.put("/deleteById.json", 8);
		rmap.put("/modifyById.json", 2);
		rmap.put("/get.json", 1);
		rmap.put("/list.json", 1);
		rmap.put("/getvo.json", 1);
		rmap.put("/listvo.json", 1);
		rmap.put("/listvoAll.json", 1);
		rmap.put("/listAll.json", 1);
		rmap.put("/batchOperate.json", 8);
		rmap.put("/listsave.json", 8);
		rmap.put("/addMasterCustomer.json", 8);
		rmap.put("/saveMasterCustomerList.json", 8);
		rmap.put("/audit.json", 2);
		rmap.put("/saveSizeHorizontalData.json", 2);
		rmap.put("/do_export", 64);
		//rmap.put("/do_master_customer_export", 64);
		//rmap.put("/importExcel.json", 64);
	}
	
	/**
	 * 过滤ajax请求。未登录返回timeout
	 * @param request
	 * @param response
	 */
	public static boolean filterXMLHttpRequest(final HttpServletRequest request, final HttpServletResponse response){
		String requestType =(String) request.getHeader("X-Requested-With"); 
		logger.debug("RequestURI:"+request.getRequestURI());
		boolean ret=false;
		try {
			if (requestType != null && requestType.equals("XMLHttpRequest")) {
				responseOutJson(response, getLoginOutResult());
				ret=true;
			}
		} catch (Exception e) {
			logger.error("error:",e);
		}
		return ret;
	}
	
	/**
	 * 过滤操作权限
	 * @param request
	 * @param response
	 * @return 无权限true
	 */
	public static boolean filterUnauthorizedRequest(
			final HttpServletRequest request, final HttpServletResponse response,SystemUser user) {
			if(user!=null){
				return filterUnauthorizedRequest(request, response, user.getUserMenuMap());
			}
			else{
				return true;
			}
	}
	
	/**
	 * 过滤操作权限
	 * @param request
	 * @param response
	 * @return 无权限true
	 */
	public static boolean filterUnauthorizedRequest(
			final HttpServletRequest request, final HttpServletResponse response,HashMap<String, String> userMenuMap) {
		String url=request.getRequestURI(); //"/hc-uc-wehcg_roleright/list.json";
		boolean ret=false;
		try {
			if(checkExcludeUrls(url)||userMenuMap==null){
	    		return false;
	    	}
			
			boolean operatFlag=checkOperateRight(url,userMenuMap);
			if (operatFlag==false) {	
				logger.debug("RequestURI Unauthorized url:"+url);
				responseOutJson(response, getUnauthorized(url));
				ret=true;
			}
		} catch (Exception e) {
			logger.error("error:",e);
		}
		return ret;
	}
	
	/**
	 * 不拦截的url检测
	 * /index.html必须返回false,否则无法 进行后续的session续期。
	 * @param url
	 * @return 不拦截的url返回true,否则返回false
	 */
	public static boolean checkExcludeUrls(String url) {
		if(url.contains("/index.html")){
			return false;
		}
		else if (CommonUtil.isExtension(url,"json")) {
            for (String excludeUrl: excludeUrlsList) {
                if (excludeUrl.equals(url)) {
                    return true;
                }
            }
        }
        else{
        	return true;
        }
        
		return false;
    }
	
	
	/** 
     * 以JSON格式输出 
     * @param response 
     */  
	public static void responseOutJson(HttpServletResponse response, Object responseObject) {  
        response.setCharacterEncoding("UTF-8");  
        response.setContentType("application/json; charset=utf-8");  
        PrintWriter out = null;  
        try {
        	String outstr=JSON.toJSONString(responseObject);
            out = response.getWriter();  
            out.append(outstr);  
            logger.debug("response:"+outstr);  
            response.flushBuffer();
        } catch (IOException e) {
        	logger.error("error:",e);  
        } finally {  
            if (out != null) {  
                out.close();  
            }  
        }  
    }  
	
	/**
     * 返回TimeOut
     * @return
     */
	public static  Map<String,Object> getLoginOutResult(){
    	Map<String, Object> resultMap =new HashMap<String, Object>();
    	ResultModel resultModel = new ResultModel();
    	resultModel.setResultCode("timeout");
    	resultModel.setMsg("登录超时，请重新登录。");
    	resultMap.put("result", resultModel);
		return resultMap;
    }
	
	/**
     * 返回无权限访问该资源
     * @return
     */
	public static  Map<String,Object> getUnauthorized(String reqURI){
    	Map<String, Object> resultMap =new HashMap<String, Object>();
    	ResultModel resultModel = new ResultModel();
    	resultModel.setResultCode("unauthorized");
    	resultModel.setMsg("当前登录用户无权限访问资源:"+reqURI);
    	resultMap.put("result", resultModel);
		return resultMap;
    }

	
	/**
	 * 检测菜单拥有的操作权限
	 * 模块的right_value大于等于下列值则拥有相应操作权限
	 * 1 浏览
	 * 2 编辑
	 * 4 增加
	 * 8 删除
	 * 16 打印设置
	 * 32 打印
	 * 64 导出
	 * 128 赋权
	 * 256 权限传递
	 * @param url
	 * @param userMenuMap
	 * @return 有权限true,无false
	 */
	public static boolean checkOperateRight(String url,HashMap<String, String> userMenuMap) {
		boolean rightFlag=false;
		String arr[]=url.substring(1).split("/");
		if(arr.length>2){
			String operateUrl="/"+arr[2];
			//原：/hc-uc-web/hcroleright/list.json
			//新：/hc-uc-web/ithceright
			String url_new="/"+arr[0]+"/"+arr[1].replace("_", "");
			String right_value=userMenuMap.get(url_new);
			//主从表请求url处理
			if(url_new.lastIndexOf("dtl")>=0){
				right_value=userMenuMap.get(url_new.substring(0,url_new.lastIndexOf("dtl")));
			}
			logger.debug("operateUrl:"+operateUrl+" right_value:"+right_value);
			
			if(StringUtils.isEmpty(right_value)){
				rightFlag=false;
			}
			else if(rmap.get(operateUrl)!=null){
				int val=rmap.get(operateUrl);
				if(Integer.parseInt(right_value)>=val){
					rightFlag=true;
				}
				else{
					rightFlag=false;
				}
			}
			else if(Integer.parseInt(right_value)>0){
				//没有定义按钮级别权限的默认有权限
				rightFlag=true;
			}
		}
		return rightFlag;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*HashMap<String, String> userMenuMap=new HashMap<>();
		userMenuMap.put("/hc-sd-web/sdorhcettinginfo", "127");
		userMenuMap.put("/hc-uc-web/itgrolhcht", "11");
		userMenuMap.put("/hc-mdm-web/basdelihcpoint", "0");
		userMenuMap.put("/hc-uc-web/itgrolerighc "127");*/
		//System.out.println("ret:"+checkOperateRight("/hc-uc-web/itg_rolerighhcst.json", userMenuMap));
		//System.out.println("ret:"+checkOperateRight("/hc-uc-web/itg_roleright/hcvo.json", userMenuMap));
		//System.out.println("ret:"+checkOperateRight(url, userMenuMap));
		//System.out.println("ret:"+checkOperateRight(url, userMenuMap));
		/*String url="/hc-uc-web/itg_rolerightdtlhct.json";
		System.out.println("ret:"+checkOperateRight(url, userMenuMap));
		System.out.println(url.substring(0,url.lastIndexOf("dtl")));*/
	}
}
