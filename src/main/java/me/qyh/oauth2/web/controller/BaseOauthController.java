package me.qyh.oauth2.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.qyh.oauth2.entity.OauthType;
import me.qyh.oauth2.exception.Oauth2InvalidStateException;
import me.qyh.utils.Validators;
import me.qyh.web.controller.BaseController;

public class BaseOauthController extends BaseController {

	protected static final String SECRET_KEY = "secretKey";
	protected static final String OAUTH_USER = "oauthUser";
	protected static final String STATE = "state";

	/**
	 * 检查state值是否匹配，防止csrf
	 * 
	 * @param request
	 * @throws Oauth2InvalidStateException
	 */
	protected void checkState(HttpServletRequest request, OauthType type) {
		HttpSession session = request.getSession(false);
		boolean valid = false;
		if (session != null) {
			String state = (String) session.getAttribute(STATE);
			session.removeAttribute(STATE);
			String _state = request.getParameter(STATE);
			valid = !(Validators.isEmptyOrNull(_state, true)
					|| !_state.equals(state));
		}
		if (!valid) {
			throw new Oauth2InvalidStateException(type,
					"oauth2 server传来的state无法被找到或者匹配失败，无法完成认证");
		}
	}

}
