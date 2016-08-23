package com.hc.scm.common.intercepter;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hc.scm.common.base.mapper.BaseCrudMapper;
import com.hc.scm.common.model.SystemUser;
import com.hc.scm.common.utils.CommonUtil;
import com.hc.scm.common.utils.LogTablesCache;
import com.hc.scm.common.utils.SessionUtils;
import com.hc.scm.common.utils.SpringComponent;

@Aspect
@Component
public class LogSaveAspect {
	Logger logger = LoggerFactory.getLogger(LogSaveAspect.class);
	
	@Pointcut("execution(* com.hc.scm.common.base.dal.BaseCrudDaoImpl.deleteById(..)) " +
			"|| execution(* com.hc.scm.common.base.dal.BaseCrudDaoImpl.add(..)) " +
			"|| execution(* com.hc.scm.common.base.dal.BaseCrudDaoImpl.modifyById(..)) " +
			"|| execution(* com.hc.scm.common.base.dal.BaseCrudDaoImpl.save(..)) " +
			"|| execution(* com.hc.scm.common.base.dal.BaseCrudDaoImpl.deleteByPrimarayKeyForModel(..)) " +
			"|| execution(* com.hc.scm.*.dao.dal.impl..*.deleteById(..))" + 
			"|| execution(* com.hc.scm.*.dao.dal.impl..*.add(..))" + 
			"|| execution(* com.hc.scm.*.dao.dal.impl..*.modifyById(..))" +
			"|| execution(* com.hc.scm.*.dao.dal.impl..*.deleteByPrimarayKeyForModel(..))" +
			"|| execution(* com.hc.scm.*.dao.dal.impl..*.save(..))") 
	public void logPointcut() {
	}

	
	@After("logPointcut()")
	public void logSave(final JoinPoint joinPoint) throws Throwable {
		StopWatch clock = new StopWatch();
		clock.start();	// 计时开始 
		//logger.info("====logPointcut===begin===" + clock.getTime()); 
		
		 // 调用方法名称  
	     String methodName = joinPoint.getSignature().getName(); 
	     SystemUser loginUser=SessionUtils.getCurrentLoginUser();
	     String logAttribute="";
	     if(methodName.equals("add")){
	    	 logAttribute="A";
	     }else if(methodName.equals("deleteById") ||methodName.equals("deleteByPrimarayKeyForModel")){
	    	 logAttribute="D";
	     }else if(methodName.equals("modifyById")){
	    	 logAttribute="U";
	     }
	     
	     Object[] args = joinPoint.getArgs();
	     if(args != null && args.length > 0){
	    	 Object arg = args[0];
	    	 String origObjName= arg.getClass().getName();
	    	 String classPath= origObjName.substring(0,origObjName.lastIndexOf("."));
	    	 origObjName=origObjName.substring(origObjName.lastIndexOf(".")+1);
	    	 if(!LogTablesCache.getLogTables().contains(origObjName))return;
	    	 String underLineObjName=CommonUtil.convertJaveBeanStrToUnderLine(origObjName);
	    	 String underLineLogClassName=underLineObjName.replace("_"+underLineObjName.split("_")[1], "log");
	    	 String logClassName=CommonUtil.convertUnderLineStrToJaveBean(underLineLogClassName);
	    	 classPath=classPath+"."+CommonUtil.changeFirstCharUporLow(logClassName, 0);
	    	 Class<?> clazz=Class.forName(classPath);
	    	 Object logObj= clazz.newInstance();
	    	 CommonUtil.setFieldValue(logObj, "logAttribute", clazz, logAttribute);
	    	 if(loginUser!=null){
	    		 CommonUtil.setFieldValue(logObj, "logUser", clazz, loginUser.getUserName());
	    	 }else{
	    		 CommonUtil.setFieldValue(logObj, "logUser", clazz, "超级管理员");
	    	 }
	    	 CommonUtil.setFieldValue(logObj, "logTime", clazz,new Date());
	    	 BeanUtils.copyProperties(logObj, arg);
	    	 BaseCrudMapper mapper=(BaseCrudMapper)SpringComponent.getBean(logClassName+"Mapper");
	    	 mapper.insertSelective(logObj);
	    	 //logger.info("====logPointcut===end===" + clock.getTime());
	    	 
	    	 String jsonData = JSON.toJSONString(arg);
	    	 logger.info(String.format("当前记录操作日志耗时：%s ms. className：%s. methodName：%s. jsonData：%s", clock.getTime(), origObjName, methodName, jsonData));
	     }
	     
	}
}
