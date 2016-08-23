package com.hc.scm.common.annotation;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 读取缓存注解
 *
 * @author penghz
 * @date 2015-3-9
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ReadCacheAnnotation {
	/**
	 * 命名空间
	 */
	String namespace() default AnnotationConstants.NAMESPACE;

	/**
	 * 緩存key
	 */
	String assignCacheKey();

	/**
	 * 缓存服务器上缓存的时间(大于0时生效) (秒)
	 */
	int remoteExpire() default 0;
	
	int cacheStyle() default 0;
	
	Class<?> cacheType() default Object.class;
}
