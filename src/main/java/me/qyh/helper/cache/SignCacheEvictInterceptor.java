package me.qyh.helper.cache;

import java.lang.reflect.Method;

import me.qyh.exception.SystemException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class SignCacheEvictInterceptor implements MethodInterceptor {

	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private SignCacheStore signCacheStore;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		SignCacheEvict evict = AnnotationUtils.getAnnotation(method, SignCacheEvict.class);
		Cache cache = cacheManager.getCache(evict.cacheName());
		if (cache != null) {
			StandardEvaluationContext context = CacheHelper.getContext(method, invocation.getArguments());
			SpelExpressionParser parser = new SpelExpressionParser();
			Object condition = parser.parseExpression(evict.condition()).getValue(context);
			if (condition == null || !(condition instanceof Boolean)) {
				throw new SystemException("SignCache Annotation中的condition表达式不能为空，并且必须是布尔表达式");
			}
			if ((Boolean) condition) {
				Object cacheKey = parser.parseExpression(evict.cacheKey()).getValue(context);
				cache.evict(cacheKey);
				signCacheStore.evict(cacheKey);
			}
		}
		return invocation.proceed();
	}

}
