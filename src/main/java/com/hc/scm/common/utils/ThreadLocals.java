package com.hc.scm.common.utils;
import java.util.HashMap;


public class ThreadLocals {
	
	public static final ThreadLocal<HashMap<String,String>> headersThreadLocal=new ThreadLocal<HashMap<String,String>>();
	
	public static final ThreadLocal<String> errorMsgs = new ThreadLocal<String>();
	
	 public static void setErrorMsgs(String msg)
	    {
		 errorMsgs.set(errorMsgs.get()+msg);
	    }

	    public static String getErrorMsgs()
	    {
	        return errorMsgs.get();
	    }
	    
	    
	public static void setHeaderInfo(HashMap<String,String> headerMap){
		headersThreadLocal.set(headerMap);
	}
	
	public static HashMap<String,String> getHeaderInfo(){
		return headersThreadLocal.get();
	}

}
