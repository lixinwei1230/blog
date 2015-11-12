package me.qyh.helper.cache;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import me.qyh.exception.SystemException;

@Component
public class SignCacheEvitInterceptor implements MethodInterceptor {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private SignCacheStore signCacheStore;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		SignCacheEvit evit = AnnotationUtils.getAnnotation(method, SignCacheEvit.class);
		Cache cache = cacheManager.getCache(evit.cacheName());
		if (cache != null) {
			StandardEvaluationContext context = CacheHelper.getContext(method, invocation.getArguments());
			SpelExpressionParser parser = new SpelExpressionParser();
			Object condition = parser.parseExpression(evit.condition()).getValue(context);
			if (condition == null || !(condition instanceof Boolean)) {
				throw new SystemException("SignCache Annotation中的condition表达式不能为空，并且必须是布尔表达式");
			}
			if ((Boolean) condition) {
				Object cacheKey = parser.parseExpression(evit.cacheKey()).getValue(context);
				cache.evict(cacheKey);
				signCacheStore.remove(cacheKey);
			}
		}
		return invocation.proceed();
	}

}
