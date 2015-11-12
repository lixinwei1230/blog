package me.qyh.web.tag.token;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TokenInterceptor extends HandlerInterceptorAdapter {

	@Value("${config.duplicateSubmisionPage}")
	private String duplicateSubmissionPage;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;

			Token token = AnnotationUtils.getAnnotation(handlerMethod.getMethod(), Token.class);

			if (token != null) {
				HttpSession session = request.getSession(true);
				synchronized (session) {
					if (!TokenHelper.validToken(request)) {
						handleInvalidToken(request, response);
						return false;
					}
				}
			}
		}

		return super.preHandle(request, response, handler);

	}

	protected void handleInvalidToken(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (!response.isCommitted()) {
			request.getRequestDispatcher(duplicateSubmissionPage).forward(request, response);
		}
	}

}
