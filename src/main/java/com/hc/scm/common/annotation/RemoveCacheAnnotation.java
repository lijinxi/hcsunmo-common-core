package com.hc.scm.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 删除缓存注解
 *
 * @author penghz
 * @date 2015-3-9
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RemoveCacheAnnotation {
	/**
	 * 命名空间
	 */
	String namespace() default AnnotationConstants.NAMESPACE;
	/**
	 *	緩存key
	 */
	String assignCacheKey();
}
