package me.qyh.helper.cache;

import java.lang.reflect.Method;

import me.qyh.exception.SystemException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class SignCacheInterceptor implements MethodInterceptor {
	
	private static final String PROCEED_RESULT = "result";

	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private SignCacheStore signCacheStore;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		SignCache signCache = AnnotationUtils.getAnnotation(method, SignCache.class);
		Cache cache = cacheManager.getCache(signCache.cacheName());
		if (cache != null) {
			StandardEvaluationContext context = CacheHelper.getContext(method, invocation.getArguments());
			SpelExpressionParser parser = new SpelExpressionParser();
			Object cacheKey = parser.parseExpression(signCache.cacheKey()).getValue(context);
			ValueWrapper cached = cache.get(cacheKey);
			if (cached != null) {
				return cached.get();
			}
			Object result = invocation.proceed();
			context.setVariable(PROCEED_RESULT, result);
			Object condition = parser.parseExpression(signCache.condition()).getValue(context);
			if (condition == null || !(condition instanceof Boolean)) {
				throw new SystemException("SignCache Annotation中的condition表达式不能为空，并且必须是布尔表达式");
			}
			if (result != null && (Boolean) condition) {
				Sign sign = signCacheStore.getSign(cacheKey);
				if (sign == null) {
					signCacheStore.put(cacheKey, new Sign(signCache.periodSec(), signCache.hits()));
				} else {
					long now = System.currentTimeMillis();
					if (!sign.addHit(now)) {
						signCacheStore.evict(cacheKey);
						signCacheStore.put(cacheKey, new Sign(signCache.periodSec(), signCache.hits()));
					} else if (sign.cache(now)) {
						cache.put(cacheKey, result);
						signCacheStore.evict(cacheKey);
					}
				}
			}
			return result;
		}
		return invocation.proceed();
	}
}
