package me.qyh.oauth2.web.controller;

import me.qyh.oauth2.Oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("oauth2/qq")
public class QQOauth2Controller extends OauthAuthorizeController {

	@Autowired
	private Oauth2 qqOauth2;

	@Override
	Oauth2 oauth2() {
		return qqOauth2;
	}

}
