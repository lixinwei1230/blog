package me.qyh.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.qyh.web.Webs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class ValidateCodeUsernamePasswordAuthencationFilter extends UsernamePasswordAuthenticationFilter{
	
	private static final String VALIDATE_CODE_NAME = "validateCode";
	private String validateCodeName = VALIDATE_CODE_NAME ;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if(!Webs.matchValidateCode(request.getSession(false), obtainValidateCode(request))){
			throw new I18NMessageAuthencationException("error.validateCode");
		};
		return super.attemptAuthentication(request, response);
	}

	public String getValidateCodeName() {
		return validateCodeName;
	}

	public void setValidateCodeName(String validateCodeName) {
		this.validateCodeName = validateCodeName;
	}

	protected String obtainValidateCode(HttpServletRequest request) {
		return request.getParameter(validateCodeName);
	}
	
}
