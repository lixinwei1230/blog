package me.qyh.helper.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import me.qyh.exception.BusinessAccessDeinedException;
import me.qyh.exception.BusinessException;
import me.qyh.exception.BusinessRuntimeException;
import me.qyh.security.UserContext;

@Component
@Aspect
public class ExceptionLog {

	private static final Logger logger = LoggerFactory.getLogger("errorLogger");

	@AfterThrowing(throwing = "e", value = "execution(* me.qyh.service.impl.*.*(..))")
	public void afterThrowing(JoinPoint jp, Throwable e) {
		if (needLog(e)) {
			logger.error("当前用户： " + UserContext.getUser() + ",错误信息：" + e.getMessage(), e);
		}
	}

	private boolean needLog(Throwable e) {
		return !(e instanceof BusinessException) && !(e instanceof BusinessAccessDeinedException)
				&& !(e instanceof BusinessRuntimeException);
	}

}
