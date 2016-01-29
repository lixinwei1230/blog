package me.qyh.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.qyh.bean.Info;
import me.qyh.dao.LoginInfoDao;
import me.qyh.entity.LoginInfo;
import me.qyh.web.Webs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 登录成功处理器
 * 
 * @author henry.qian
 *
 */
@Component("loginSuccessHandler")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	private LoginInfoDao loginInfoDao;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String remoteAddress = Webs.getClientIpAddress(request);
		LoginInfo info = new LoginInfo();
		info.setRemoteAddress(remoteAddress);
		info.setUser(UserContext.getUser());
		info.setLoginDate(new Date());
		
		loginInfoDao.insert(info);
		
		if(Webs.isAjaxRequest(request)){
			Webs.writeInfo(response, new Info(true));
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}
}
