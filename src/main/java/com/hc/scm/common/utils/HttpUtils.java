package com.hc.scm.common.utils;





import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hc.scm.common.constans.SysConstans;



/**
 * http请求
 * 
 * @author penghz
 * 
 */
public class HttpUtils
{
	protected  static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
    /**
     * post请求 ，超时默认30秒
     * 
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params)
            throws IOException
    {
    	setAuthorityParams(params);
        return post(url, params, 20,false);
    }
    
    /**
     * 带签名的post请求
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String postWithAuthority(String url, Map<String, String> params)
            throws IOException
    {
    	setAuthorityParams(params);
        return post(url, params, 20,false);
    }

    /**
     * post请求
     * 
     * @param url
     * @param params
     * @param timeout
     *                超时时间，秒
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params,
            int timeout) throws IOException
    {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout",
                timeout * 1000);
        httpclient.getParams().setBooleanParameter(
                "http.protocol.expect-continue", false);
        String retVal = "";
        try
        {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            if (params != null)
            {
                for (Map.Entry<String, String> param : params.entrySet())
                {
                    formparams.add(new BasicNameValuePair(param.getKey(), param
                            .getValue()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
                    HTTP.UTF_8);
            HttpPost httppost = new HttpPost(url);
            setHeaderInfo(httppost);
            httppost.setEntity(entity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            retVal = new String(httpclient.execute(httppost, responseHandler)
                    .getBytes(HTTP.ISO_8859_1), HTTP.UTF_8);
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }
    
   
    /**
     * post请求
     * 
     * @param url
     * @param params
     * @param timeout
     *                超时时间，秒
     * @param isParseReturn
     *                是否转码返回值
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params,
            int timeout,boolean isParseReturn) throws IOException
    {
    	StopWatch clock = new StopWatch();
    	clock.start();
    	
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout",
                timeout * 1000);
        httpclient.getParams().setBooleanParameter(
                "http.protocol.expect-continue", false); 
        String retVal = "";
        try
        {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            if (params != null)
            {
                for (Map.Entry<String, String> param : params.entrySet())
                {
                    formparams.add(new BasicNameValuePair(param.getKey(), param
                            .getValue()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
                    HTTP.UTF_8);
            HttpPost httppost = new HttpPost(url);
            setHeaderInfo(httppost);
            httppost.setEntity(entity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            if(isParseReturn){
                retVal = new String(httpclient.execute(httppost, responseHandler)
                    .getBytes(HTTP.ISO_8859_1), HTTP.UTF_8);
            }else{
                retVal = httpclient.execute(httppost, responseHandler).toString();
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            httpclient.getConnectionManager().shutdown();
        }
        
        logger.info(String.format("===request info===[waiting:%s ms;url:%s;params:%s;reponse:%s]", 
        		clock.getTime(), url, params, retVal));
        return retVal;
    }
    
    /**
     * 设置头信息
     * @param httppost
     */
    private static void setHeaderInfo(HttpPost httppost){
//    	if(ThreadLocals.getHeaderInfo()!=null && ThreadLocals.getHeaderInfo().containsKey("Referer")){
//    		httppost.addHeader("Referer", ThreadLocals.getHeaderInfo().get("Referer"));
//    	}
//    	if(ThreadLocals.getHeaderInfo()!=null && ThreadLocals.getHeaderInfo().containsKey("Cookie")){
//    		httppost.addHeader("Cookie", ThreadLocals.getHeaderInfo().get("Cookie"));
//    	}
    }
    
    private static void setAuthorityParams(Map<String, String> params) {
//    	if(ThreadLocals.getHeaderInfo()==null ||
//    			!ThreadLocals.getHeaderInfo().containsKey("Cookie")){
    		String req_dateTime_str=String.valueOf( new Date().getTime());
    		String sign_key=SignUtils.getSign_key();
    		String sign=SignUtils.getSign(req_dateTime_str,sign_key);
    		params.put(SysConstans.REQ_DATETIME, req_dateTime_str);
    		params.put(SysConstans.SIGN_VAR, sign);
//    	}
    }
    
    public static String postJson(String url, String json, int timeout, Object... objects) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String retVal = "";
        try {
//            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//            if (params != null) {
//                for (Map.Entry<String, String> param : params.entrySet()) {
//                    formparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
//                }
//            }
            String encoding = HTTP.UTF_8;
            if(objects != null && objects.length>0){
                encoding = objects[0].toString();
            }
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, encoding);
//            HttpPost httppost = new HttpPost(NetUtil.strParseToNet(url));
            HttpPost httppost = new HttpPost(url);
//            httppost.setEntity(entity);
            StringEntity params =new StringEntity(json,encoding);  
            httppost.addHeader("content-type", "application/json");  
            httppost.setEntity(params);  
            setHeaderInfo(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            if(objects == null || objects.length ==0){
                retVal = new String(httpclient.execute(
                        httppost, responseHandler).getBytes(HTTP.ISO_8859_1),
                        HTTP.UTF_8);
            }else if(objects !=null && objects[0].equals("utf-8")){
                retVal = httpclient.execute(httppost, responseHandler);
            }else if(objects !=null && objects[0].equals("gb2312")){
                retVal = new String(httpclient.execute(httppost, responseHandler).getBytes("iso-8859-1"),"gb2312");
            }else{
                retVal = new String(httpclient.execute(
                        httppost, responseHandler).getBytes(),
                        HTTP.UTF_8);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }

    /**
     * get请求
     * 
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params)
            throws IOException
    {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", 100000);
        String retVal = "";
        try
        {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            if (params != null)
            {
                for (Map.Entry<String, String> param : params.entrySet())
                {
                    qparams.add(new BasicNameValuePair(param.getKey(), param
                            .getValue()));
                }
            }
            String paramstr = URLEncodedUtils.format(qparams, HTTP.UTF_8);
            if (StringUtils.isNotEmpty(paramstr))
            {
                url = url + "?" + paramstr;
            }
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            retVal = httpclient.execute(httpget, responseHandler);
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }

    /**
     * get请求
     * 
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params,
            String charset) throws IOException
    {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", 100000);
        String retVal = "";
        try
        {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            if (params != null)
            {
                for (Map.Entry<String, String> param : params.entrySet())
                {
                    qparams.add(new BasicNameValuePair(param.getKey(), param
                            .getValue()));
                }
            }
            String paramstr = URLEncodedUtils.format(qparams, charset);
            if (StringUtils.isNotEmpty(paramstr))
            {
                url = url + "?" + paramstr;
            }
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            retVal = httpclient.execute(httpget, responseHandler);
            retVal = new String(retVal.getBytes(HTTP.ISO_8859_1), charset);
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }

    /**
     * 异步get
     * 
     * @param url
     * @param params
     * @throws Exception
     */
    public void asynGet(String url, Map<String, String> params)
            throws Exception
    {
        /*
         * HttpClientConnection conn = null; try { URI uri = new URI(url);
         * String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
         * String host = uri.getHost() == null ? "localhost" : uri.getHost();
         * int port = uri.getPort(); if (port == -1) { if
         * (scheme.equalsIgnoreCase("http")) { port = 80; } else if
         * (scheme.equalsIgnoreCase("https")) { port = 443; } }
         * 
         * conn = new HttpClientConnection(host, port); GetRequest request =
         * null; if (params == null) { request = new GetRequest(url); } else {
         * org.xlightweb.NameValuePair[] nv = new
         * org.xlightweb.NameValuePair[params.size()]; int i = 0; for (Map.Entry<String,
         * String> param : params.entrySet()) { nv[i] = new
         * org.xlightweb.NameValuePair(param.getKey(),
         * URLEncoder.encode(param.getValue(), "UTF-8")); i ++; } request = new
         * GetRequest(url, nv); } conn.send(request); } catch (Exception e) {
         * throw e; } finally { conn.close(); }
         */
    }

    public static void main(String[] args) throws ClientProtocolException,
            IOException
    {
    	HashMap<String,String> hashmap=new HashMap<String,String>();
		
//			hashmap.put("Referer", "http://st.uc.hct.cn/hcc-web/index.html");
			
			hashmap.put("Cookie", "JSESSIONID=337E8DD098B275F9968DD202802820F2; sid=9ce255db-2b9f-43f9-8d38-0bcf42bf600d; JSESSIONID=2549BEB78522AB0BBEA6D3052C5DEFBB; sid=fb72c43e-ed30-4bad-80f4-9c80c99a5782");
		
		ThreadLocals.setHeaderInfo(hashmap);
        String url = "http://st.uc.hc.hcn/hchc-web/bas_size_type/list.json";
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("billTypeNo","DO");
        String str=post(url,params);
        System.out.println(str);
//        String code=JsonUtils.getValueByKey("sheetIdCode", str);
//        String result=JsonUtils.getValueByKey("result", str);
//        String reusltCode=JsonUtils.getValueByKey("resultCode", result);
//        System.out.println(reusltCode);
//        System.out.println(code);
        
    }

    /**
     * 得到访问url的返回状态(200正常)
     * 
     * @param url
     *                :访问地址
     * @param timeOut
     *                :设置超时时间秒
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static int getPostRetStatu(String url, Integer timeOut)
    {
        HttpResponse response = null;
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setIntParameter("http.socket.timeout",
                    timeOut * 1000);
            httpclient.getParams().setBooleanParameter(
                    "http.protocol.expect-continue", false);
            /**
             * 此处使用get请求,因为一些网站屏蔽了post请求eg:baidu
             */
            // HttpPost httpost = new HttpPost(url);
            HttpGet httpget = new HttpGet(url);
            response = httpclient.execute(httpget);
        }
        catch (Exception e)
        {
            // e.printStackTrace();
            return 404;
        }
        return response.getStatusLine().getStatusCode();
    }

  
    /**
     * @Description:获取参数路径
     * @param params
     * @return
     * @author Alvin.zengqi
     * @date 2011-6-27 下午08:55:16
     */
    public static String getParamsPath(Map<String, String> params)
    {
        String url = "";
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        // String paramstr = "";
        if (params != null)
        {
            for (Map.Entry<String, String> param : params.entrySet())
            {
                qparams.add(new BasicNameValuePair(param.getKey(), param
                        .getValue()));
                // paramstr = paramstr+param.getKey()+"="+param.getValue()+"&";
            }
            // paramstr = paramstr + "randomCode="+new Date().toString();
        }
        String paramstr = URLEncodedUtils.format(qparams, HTTP.UTF_8);
        if (StringUtils.isNotEmpty(paramstr))
        {
            url = url + "?" + paramstr;
        }
        return url;
    }
    
    /**
     * get请求
     * @param url
     * @param params
     * @param timeout 超时时间，秒
     * @param charset 编码方式
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, String> params, int timeout, String charset) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String retVal = "";
        try {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
            }
            String paramstr = URLEncodedUtils.format(qparams, charset);
            if (StringUtils.isNotEmpty(paramstr)) {
                url = url + "?" + paramstr;
            }
            HttpGet httpget = new HttpGet(url);
            
            HttpResponse resp = httpclient.execute(httpget);
            retVal = EntityUtils.toString(resp.getEntity(), charset);
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }
    /**
     * post请求
     * @param url
     * @param params
     * @param timeout 超时时间，秒
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params, int timeout, String charset) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String retVal = "";
        try {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    formparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, charset);
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(entity);
            HttpResponse resp = httpclient.execute(httppost);
            retVal = EntityUtils.toString(resp.getEntity(), charset);
        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return retVal;
    }
    
  
}