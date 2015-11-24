package me.qyh.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;

import me.qyh.bean.Info;
import me.qyh.web.Webs;
import me.qyh.web.tag.url.UrlHelper;

//注销登录成功处理器
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	private ObjectWriter objectWriter;
	@Autowired
	private UrlHelper urlHelper;
	private String logoutPath;

	public MyLogoutSuccessHandler(String logoutPath) {
		this.logoutPath = logoutPath;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authencation)
			throws IOException, ServletException {

		if (Webs.isAjaxRequest(request)) {
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(response.getOutputStream(),
					JsonEncoding.UTF8);
			objectWriter.writeValue(jsonGenerator, new Info(true));
		} else {
			response.sendRedirect(urlHelper.getUrl() + logoutPath);
		}
	}

}
