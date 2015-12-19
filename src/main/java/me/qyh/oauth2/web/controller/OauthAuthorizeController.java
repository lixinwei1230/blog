package me.qyh.oauth2.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.qyh.oauth2.Oauth2;
import me.qyh.oauth2.exception.Oauth2Exception;
import me.qyh.utils.Strings;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public abstract class OauthAuthorizeController extends BaseOauthController {
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response) throws Oauth2Exception {
		String state = Strings.uuid();
		HttpSession session = request.getSession();
		session.setAttribute(STATE, state);
		return "redirect:" + oauth2().getAuthorizeUrl(state);
	}
	
	abstract Oauth2 oauth2();

}
