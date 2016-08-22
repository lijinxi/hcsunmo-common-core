package com.hc.scm.common.annotation;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.hc.scm.common.cache.RedisClient;

@Aspect
@Component
public class ReadCacheIntercepter extends CacheBase {

	@Resource
	private RedisClient redisClient;

	@Pointcut("@annotation(com.hc.scm.common.annotation.ReadCacheAnnotation)")
	public void methodCachePointcut() {
	}

	@Around("methodCachePointcut()")
	public Object methodCacheHold(final ProceedingJoinPoint joinPoint)
			throws Throwable {
		ReadCacheAnnotation annotation = null;
		Object result = null;
		String cacheKey = "";

		try {
			// 获取目标方法
			final Method method = this.getMethod(joinPoint);
			
			annotation = method.getAnnotation(ReadCacheAnnotation.class);
			// 组合缓存KEY
			cacheKey = this.getCacheKey(joinPoint.getArgs(),
					annotation.assignCacheKey());
			
			result = redisClient.get(cacheKey);
			// 保存缓存key值 
			//this.saveRedisKey(cacheKey);
			//result =1;
			if(!this.isSameRetrunType(result, joinPoint)){
				result = null;
				redisClient.delete(cacheKey);
				logger.warn("缓存对象不匹配，出现异常。cacheKey:"+cacheKey);
			}
			
			
		} catch (Throwable ex) {
			logger.error("Caching on " + joinPoint.toShortString()
					+ " aborted due to an error.", ex);
			return joinPoint.proceed();
		}
		logger.info("get Rediscache Key:" + cacheKey+" result:"+(result==null?false:true));
		
		if (result == null) {
			// 缓存命中失败,执行方法从DB获取数据
			result = joinPoint.proceed();
			redisClient.set(cacheKey, result, annotation.remoteExpire());
			logger.info("set Rediscache Key:" + cacheKey);
		}
		
		return result;
	}
}