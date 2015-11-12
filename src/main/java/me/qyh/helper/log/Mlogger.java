package me.qyh.helper.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import me.qyh.entity.User;
import me.qyh.security.UserContext;

@Component
@Aspect
public class Mlogger {

	private static final Logger logger = LoggerFactory.getLogger("mlog");

	@Around("execution(* *(..)) && @annotation(me.qyh.helper.log.Mlog)")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = point.proceed();
		User current = UserContext.getUser();
		logger.info("#执行方法：{}(参数:{}): 结果：{}， 耗时： {}s，当前登录用户:{}", new Object[] { point.getSignature(), point.getArgs(),
				result == null ? "" : result, System.currentTimeMillis() - start, current == null ? "" : current });
		return result;

	}

}
