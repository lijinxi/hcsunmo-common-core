package com.hc.scm.common.annotation;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.hc.scm.common.cache.RedisClient;

@Aspect
@Component
public class RemoveCacheIntercepter extends CacheBase {

	@Resource
	private RedisClient redisClient;

	@Pointcut("@annotation(com.hc.scm.common.annotation.RemoveCacheAnnotation)")
	public void removeCachePointcut() {
	}

	@AfterReturning(pointcut = "removeCachePointcut()")
	public void methodCacheHold(final JoinPoint joinPoint) throws Throwable {
		try {
			// 获取目标方法
			final Method method = this.getMethod(joinPoint);
			final RemoveCacheAnnotation annotation = method
					.getAnnotation(RemoveCacheAnnotation.class);
			String cacheKey = this.getCacheKey(joinPoint.getArgs(),
					annotation.assignCacheKey());

			logger.info("删除缓存---Redis缓存:cacheKey---" + cacheKey);
			if (cacheKey.endsWith("*")) {//以*结尾的特殊处理
				this.delMulRedisCache(cacheKey);
			} else {
				redisClient.delete(cacheKey);
				this.delRedisKey(cacheKey);
			}
		} catch (Throwable ex) {
			logger.error("Remove caching via " + joinPoint.toShortString()
					+ " aborted due to an error.", ex);
		}
	}
}
