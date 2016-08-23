package com.hc.scm.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.hc.scm.common.constans.SysConstans;
import com.hc.scm.common.model.SystemUser;


/**
 * 通用方法类
 * @author wugy
 *
 */
public class CommonUtil {
	public static final String DNS_SERVIER="uc.hct.cn";
	
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 手机号码检测方法
	 * 13(老)号段：130、131、132、133、134、135、136、137、138、139
	 * 15(新)号段：150、151、152、153、154、155、156、157、158、159
	 * 18(3G)号段：180、181、182、183、184、185、186、187、188、189
	 * 
	 * 130：中国联通，GSM
	 * 131：中国联通，GSM
	 * 132：中国联通，GSM
	 * 133：中国联通转给中国电信，CDMA
	 * 134：中国移动，GSM
	 * 135：中国移动，GSM
	 * 136：中国移动，GSM
	 * 137：中国移动，GSM
	 * 138：中国移动，GSM
	 * 139：中国移动，GSM
	 * 
	 * 145：中国联通GSM
	 * 147：中国移动GSM
	 * 
	 * 150：中国移动，GSM
	 * 151：中国移动，GSM
	 * 152：中国联通，暂时未对外放号
	 * 153：中国联通转给中国电信，CDMA
	 * 154：154号段暂时没有分配，估计是因为154的谐音是“要吾死”，这样的手机号码谁敢要啊？
	 * 155：中国联通，GSM
	 * 156：中国联通，GSM
	 * 157：中国移动，GSM
	 * 158：中国移动，GSM
	 * 159：中国移动，GSM
	 * 
	 * 180：中国电信，3G，尚未开始对外放号
	 * 181：3G服务的手机号段，目前没有分配给哪个运营商，也尚未开始对外放号
	 * 182：3G服务的手机号段，目前没有分配给哪个运营商，也尚未开始对外放号
	 * 183：3G服务的手机号段，目前没有分配给哪个运营商，也尚未开始对外放号
	 * 184：3G服务的手机号段，目前没有分配给哪个运营商，也尚未开始对外放号
	 * 185：中国联通，3G，尚未开始对外放号
	 * 186：中国联通，3G，尚未开始对外放号
	 * 187：中国移动，3G，尚未开始对外放号
	 * 188：中国移动，3G，目前TD测试服务在部分城市对外放号
	 * 189：中国电信，3G，CDMA，天翼189，2008年底开始对外放号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		if(mobiles==null || mobiles.equals("")){
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");  
        Matcher m = p.matcher(mobiles);  
        return m.matches();  
	}

	
	/**
	 * 数字检测
	 * @param val
	 * @return
	 */
	public static boolean isNumber(String val) {
		if(val==null || val.equals("")){
			return false;
		}
		if(Pattern.matches("^-{0,1}\\d*\\.{0,1}\\d+$",val)){
			return   true;  
		}
		else{
			return false;
		}
	}
	
	
    
    /**
     * 取得指定长度的随机数字
     * @param length
     * @return
     */
	public static String getRandomCode(int length)
    {
    	char[] codeList= {'0','1', '2', '3', '4', '5', '6', '7', '8', '9' };
    	StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int len=codeList.length;
        for (int i = 0; i < length; i++)
        {
            sb.append(codeList[random.nextInt(len)]);
        }
        return sb.toString();
    }
	
	
	/**
	 * 取得本机IP地址
	 * @return
	 */
	public static String getIPaddr(){
		String ip="";
		try{
			InetAddress addr = InetAddress.getLocalHost();
			ip=addr.getHostAddress();//获得本机IP
			//String address=addr.getHostName();//获得本机名称			
		}
		catch(Exception e){
			System.out.println("getIPaddr() error."+e);
		}
		return ip;
	} 
	
	
	/**
	 * 检测字符串首字母是否为字母
	 * @param val
	 * @return 是返回true;否返回false
	 */
	public static boolean isEnglishWord(String val) {
		if (val == null || val.equals("")) {
			return false;
		}
		char c = val.charAt(0);
		if(((c>='a'&&c<='z') || (c>='A'&&c<='Z'))){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 取出字符串的数字，包括负数
	 * @param s
	 * @return
	 */
	public static String getDigitfromStr(String s) {
		String ret="";
		for (int i = 0; i < s.length(); i++) {
			char x = s.charAt(i);
			if (Character.isDigit(x) == true || x=='-') {
				ret+=x;
			}
		}
		return ret;
	}
	
	/**
	 * 取出字符串的第一个数字的位置，包括负数
	 * @param s
	 * @return
	 */
	public static int getDigitPosfromStr(String s) {
		int ret=0;
		for (int i = 0; i < s.length(); i++) {
			char x = s.charAt(i);
			if (Character.isDigit(x) == true || x=='-') {
				ret=i;
				break;
			}
		}
		return ret;
	} 
	
    
    /**
     * 特殊字符转HTML编码
     * @param s
     * @return
     */
	public static String htmlEncode(String s) {
		String ret=s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">",
				"&gt;").replaceAll("\"", "&quot;");
		return ret;
    }
    
    /**
     * HTML编码解释转码
     * @param s
     * @return
     */
	public static String htmlDecode(String s) {
		String ret = s.replaceAll("&amp;", "&").replaceAll("&lt;", "<")
				.replaceAll("&gt;", ">").replaceAll("&quot;", "\"");
		return ret;
	}
	
    /**
	 * 对字符串进行MD5加密
	 * 
	 * @param plainText
	 *            String
	 * @return String
	 */
	public static String md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * javabean驼峰格式转下划线格式
	 * 如：testName test_name
	 * @param javeBeanStr
	 * @return
	 */
	public static String convertJaveBeanStrToUnderLine(String javeBeanStr){
		StringBuffer  buf = new StringBuffer();
		Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(javeBeanStr);
        while(m.find()){
            m.appendReplacement(buf, "_"+m.group(0));
        }
        m.appendTail(buf);
		return buf.toString().toLowerCase();
	}

	/**
	 * 下划线格式转javabean驼峰格式
	 * 如： test_name testName
	 * @param underLineStr
	 * @return
	 */
	public static String convertUnderLineStrToJaveBean(String underLineStr){
		StringBuffer  buf = new StringBuffer(underLineStr);
		Matcher mc = Pattern.compile("_").matcher(underLineStr);
		int i = 0;
		while (mc.find()) {
			int position = mc.end() - (i++);
			buf.replace(position - 1, position + 1,
					buf.substring(position, position + 1).toUpperCase());
		}
		return buf.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void object2MapWithoutNull(Object obj, Map map)
	    		throws IllegalArgumentException, IllegalAccessException {
	    	   
	    		Field[] fields = obj.getClass().getDeclaredFields();
	    		for (int j = 0; j < fields.length; j++) {
	    		    fields[j].setAccessible(true);
		    		
		    		if(fields[j].get(obj) != null){
		    			if((fields[j].get(obj) instanceof Date)){
		    				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		    				map.put(fields[j].getName(), sdf.format(fields[j].get(obj)));
		    			}else{
		    				map.put(fields[j].getName(), fields[j].get(obj));
		    			}
		    		}else {
		    			map.put(fields[j].getName(),"");
					}
	    		}
	    		
	    		Field[] fields2 = obj.getClass().getSuperclass().getDeclaredFields();
	    		for (int j = 0; j < fields2.length; j++) {
	    			fields2[j].setAccessible(true);
		    		
		    		if(fields2[j].get(obj) != null){
		    			if((fields2[j].get(obj) instanceof Date)){
		    				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		    				map.put(fields2[j].getName(), sdf.format(fields2[j].get(obj)));
		    			}else{
		    				map.put(fields2[j].getName(), fields2[j].get(obj));
		    			}
		    		}else {
		    			map.put(fields2[j].getName(),"");
					}
	    		}
	    		
	   }
	 
	  /**
      * 通过反射获得某方法信息
      * @param owner
      * @param methodName
      * @param args
      * @return
      * @throws Exception
      */
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
	      Class ownerClass = owner.getClass();
	      Class[] argsClass = new Class[args.length];
	      for (int i = 0, j = args.length; i < j; i++) {
	          argsClass[i] = args[i].getClass();
	      }
	     Method method = ownerClass.getMethod(methodName, argsClass);
	     return method.invoke(owner, args);
      }
	 
    /**
     * 通过反射设置属性的值
     * @param obj
     * @param fieldname
     * @param clazz
     * @param value
     */
	public static void setFieldValue(Object obj,String fieldname,Class<?> clazz,Object value){
	    	Field filed;
			try {
				filed = clazz.getDeclaredField(fieldname);
				filed.setAccessible(true);
				filed.set(obj, value);
			} catch (NoSuchFieldException e) {
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
			
	    }
	
	/**
	 * 通过反射获得属性的值
	 * @param obj
	 * @param fieldname
	 * @param clazz
	 * @return
	 */
	public static Object getFieldValue(Object obj,String fieldname,Class<?> clazz){
		Field filed;
		try {
			filed = clazz.getDeclaredField(fieldname);
			filed.setAccessible(true);
			return filed.get(obj);
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		return null;
	}
	
	 /**
     * 设置单个实体的默认属性
     * @param entity
     * @param req
     * @param reqType  0:新增，1：修改
	 * @param clazz
	 * @param date
     */

	public static void setEntityDefaultField(Object entity,int reqType,
    		SystemUser systemUser ,Class<?> clazz,Date date){
		if(reqType==0){
			setFieldValue(entity, "createTime", clazz, date);
			setFieldValue(entity, "creator", clazz,systemUser.getUserName() );
		}
		setFieldValue(entity, "modifyTime", clazz, date);
		setFieldValue(entity, "modifier", clazz, systemUser.getUserName());
    }
	
	/**
	 * 首字母大小写转化
	 * @param oldStr
	 * @param changeType 0：首字母转大写，1：首字母转小写
	 * @return
	 */
	public static String changeFirstCharUporLow(String oldStr,int changeType){
		if(changeType==0){
		return  oldStr=oldStr.replaceFirst(oldStr.substring(0, 1)
				,oldStr.substring(0, 1).toUpperCase())  ;
		}else if(changeType==1){
			return  oldStr=oldStr.replaceFirst(oldStr.substring(0, 1)
					,oldStr.substring(0, 1).toLowerCase())  ;
		}
		return oldStr;
	}
	

	/**
	 * 获得对象所有属性名称
	 * @param obj
	 * @return
	 */
	public static List<String> getFieldNames(Object obj){
		List<String> retList=new ArrayList<String>();
		Field[] fields=obj.getClass().getDeclaredFields();
		for(Field field:fields){
			retList.add(field.getName());
		}
		return retList;
	}
	
	/**
	 * 是否存在某属性
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static boolean isContainsField(Object obj,String fieldName){
		if(getFieldNames(obj).contains(fieldName)){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否存在某属性
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static boolean isContainsField(List<String> fieldList,String fieldName){
		if(fieldList.contains(fieldName)){
			return true;
		}
		return false;
	}
	
	public static String getHostUrl(){
		String hc_host=PropertiesUtils.getPropertieValue("hc_host");
		if(StringUtils.isNotBlank(hc_host)){
			
			 return hc_host	;
		}	
	return System.getProperty("env")+"."+DNS_SERVIER;
		//return "test"+"."+DNS_SERVIER;
	}
	
	/**
	 * 设置属性值 (没有指定类型)
	 * @param obj
	 * @param fieldname
	 * @param clazz
	 * @param value
	 */
	public static void setFieldValue2(Object obj,String fieldname,Class<?> clazz,String value){
		String type;
		try {
			type = obj.getClass().getDeclaredField(fieldname).getGenericType().toString();
			Object ObjValue;
			if(type.indexOf("int")>-1 || type.indexOf("Integer")>-1){
				 ObjValue=Integer.valueOf(value);
			}else if(type.indexOf("double")>-1 || type.indexOf("Double")>-1){
				 ObjValue=Double.valueOf(value);
			}else if(type.indexOf("long")>-1 || type.indexOf("Long")>-1){
				 ObjValue=Long.valueOf(value);
			}else if(type.indexOf("BigDecimal")>-1){
				 ObjValue=new BigDecimal(value);
			}else if(type.indexOf("boolean")>-1 || type.indexOf("Boolean")>-1){
				 ObjValue=Boolean.valueOf(value);
			}else if(type.indexOf("short")>-1 || type.indexOf("Short")>-1){
				 ObjValue=Short.valueOf(value);
			}else{
				ObjValue=String.valueOf(value);
			}
			setFieldValue(obj, fieldname, clazz, ObjValue);
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		}
		
	}
	
	/**
	 * 获取文件或url扩展名
	 * @param fileName、url
	 * @return
	 */
	public static String getFileExtension(String fileName) {
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}
	
	/**
	 * 文件或url扩展名是否为指定的扩展名
	 * @param fileName
	 * @param extension
	 * @return
	 */
	public static boolean isExtension(String fileName,String extension) {
		return (extension.equalsIgnoreCase(CommonUtil.getFileExtension(fileName)))? true:false;
	}
	
	/**
	 * 签名的KEY
	 * @return
	 */
	public static String getSign_key(){
		String sign_key=PropertiesUtils.getPropertieValue("sign_key");
		sign_key=StringUtils.isNotBlank(sign_key)?sign_key:SysConstans.SIGN_KEY;
		return sign_key;
	}
	
	  
    public static String   inputStream2String(InputStream   is)   throws   IOException{
        ByteArrayOutputStream   baos   =   new   ByteArrayOutputStream();
        int   i=-1;
        while((i=is.read())!=-1){
        baos.write(i);
        }
       return   baos.toString();
} 
	
	public static void main(String[] agrs){
		String time= String.valueOf(new Date().getTime());
		
		System.out.println(time);
		System.out.println(CommonUtil.md5(time+"adfkafqpfioqewriytibvnkadfhakaefpewqr"));
//		SystemUser s=new SystemUser();
//		try {
//			setFieldValue2(s, "ss", s.getClass(), "1.8");
//			int i=0;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//		}
	}
}
