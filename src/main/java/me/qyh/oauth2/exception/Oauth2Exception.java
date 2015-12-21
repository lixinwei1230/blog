package me.qyh.oauth2.exception;

import me.qyh.oauth2.entity.OauthType;

import org.springframework.security.core.AuthenticationException;

/**
 * 在向服务商请求oauth2信息过程中发生的异常，包括连接、解析异常等
 * @author mhlx
 *
 */
public class Oauth2Exception extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OauthType type;

	public Oauth2Exception(OauthType type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	public Oauth2Exception(OauthType type, String message) {
		super(message);
		this.type = type;
	}

	public OauthType getType() {
		return type;
	}

}
