package me.qyh.web.tag.token;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.qyh.utils.Validators;

public class TokenHelper {

	public static final String TOKEN_NAME_FIELD = "spring.token.name";
	public static final Random random = new Random();

	public static String setToken(HttpServletRequest request,
			String tokenName) {
		String token = generateGUID();
		request.getSession(true).setAttribute(tokenName, token);
		return token;
	}

	public static String generateGUID() {
		return new BigInteger(165, random).toString(36).toUpperCase();
	}

	public static String getTokenName(HttpServletRequest request) {
		Map<String, String[]> params = request.getParameterMap();
		if (!params.containsKey(TOKEN_NAME_FIELD)) {
			return null;
		}

		String[] tokenNames = params.get(TOKEN_NAME_FIELD);
		if (Validators.isEmptyOrNull(tokenNames)) {
			return null;
		}

		return tokenNames[0];
	}

	public static String getToken(String tokenName,
			HttpServletRequest request) {
		if (tokenName == null) {
			return null;
		}
		Map<String, String[]> params = request.getParameterMap();
		String[] tokens = params.get(tokenName);
		if (Validators.isEmptyOrNull(tokens)) {
			return null;
		}

		return tokens[0];
	}

	public static boolean validToken(HttpServletRequest request) {
		String tokenName = getTokenName(request);

		if (tokenName == null) {
			return false;
		}

		String token = getToken(tokenName, request);

		if (token == null) {
			return false;
		}

		HttpSession session = request.getSession();
		String sessionToken = (String) session.getAttribute(tokenName);
		if (!token.equals(sessionToken)) {
			return false;
		}
		session.removeAttribute(tokenName);

		return true;
	}
}
