package me.qyh.helper.cache;

import java.lang.reflect.Method;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public final class CacheHelper {

	public static StandardEvaluationContext getContext(Method method,
			Object[] args) {
		ParameterNameDiscoverer paramDiscoverer = new DefaultParameterNameDiscoverer();
		StandardEvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < args.length; i++) {
			context.setVariable("a" + i, args[i]);
			context.setVariable("p" + i, args[i]);
		}
		String[] parameterNames = paramDiscoverer.getParameterNames(method);
		if (parameterNames != null) {
			for (int i = 0; i < parameterNames.length; i++) {
				context.setVariable(parameterNames[i], args[i]);
			}
		}
		return context;
	}

}
