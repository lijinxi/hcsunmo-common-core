package com.hc.scm.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.server.ServletServerHttpRequest;

public class RequestBodyContentHandler {
	
	/**
	 * 获取body请求的内容
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static String getBodyContent(HttpServletRequest req) throws Exception{
		  String ret=null;
		  HttpInputMessage inputMessage = new ServletServerHttpRequest(req);
	      ret= CommonUtil.inputStream2String(inputMessage.getBody());
		  return ret;
	}

}
