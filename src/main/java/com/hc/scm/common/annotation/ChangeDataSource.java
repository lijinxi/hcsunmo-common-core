package com.hc.scm.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hc.scm.common.constans.SysConstans;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ChangeDataSource {
	
	String DBkey() default SysConstans.MDM_MYCAT;

}
