package me.qyh.oauth2.web.controller;

import javax.servlet.http.HttpSession;

import me.qyh.oauth2.Oauth2;
import me.qyh.oauth2.Oauth2Provider;
import me.qyh.oauth2.entity.OauthType;
import me.qyh.utils.Strings;
import me.qyh.utils.Validators;
import me.qyh.web.InvalidParamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OauthLoginController extends BaseOauthController {

	@Autowired
	private Oauth2Provider provider;

	@RequestMapping(value = "oauth2/{type}/login", method = RequestMethod.GET)
	public String login(@PathVariable("type") String type,HttpSession session) {
		Oauth2 oauth2 = getOauth2(type);
		if (oauth2 == null) {
			throw new InvalidParamException();
		}
		String state = Strings.uuid();
		session.setAttribute(STATE, state);
		return "redirect:" + oauth2.getAuthorizeUrl(state);
	}
	
	private Oauth2 getOauth2(String type){
		if(!Validators.isEmptyOrNull(type,true)){
			type = type.toUpperCase();
			OauthType _type = null;
			try {
				_type = OauthType.valueOf(type);
				return provider.seekOauth2(_type);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
}
