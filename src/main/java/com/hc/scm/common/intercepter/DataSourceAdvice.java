package com.hc.scm.common.intercepter;



import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hc.scm.common.constans.SysConstans;
import com.hc.scm.common.utils.DataSourceSwitch;
import com.hc.scm.common.utils.MycatTablesCache;



@Aspect
@Component
public class DataSourceAdvice {

	
	@Pointcut(value="execution(* com.hc.scm.common.base.web.BaseCrudController.saveMasterCustomerList(..)) ||" +
			" execution(* com.hc.scm.common.base.web.BaseCrudController.batchOperate(..)) ||" +
			" execution(* com.hc.scm.common.base.web.BaseCrudController.saveSizeHorizontalData(..)) ||" +
			" execution(* com.hc.scm.common.base.web.BaseCrudController.importExcel(..)) ||" +
			" @annotation(com.hc.scm.common.annotation.ChangeDataSource)")
	public void dateSourcePointcut() {
	}

	/**
	 * 全局表切换数据源 mycat
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before("dateSourcePointcut()")
	public void changeDataSource(final JoinPoint joinPoint) throws Throwable {
		String classname= joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class).value()[0]
				.toString().replace("\\", "").replace("/", "");
		if(MycatTablesCache.getMycatTables().contains(classname)){
			DataSourceSwitch.setCurrentDataSource(SysConstans.MDM_MYCAT);
		}
	}
}
