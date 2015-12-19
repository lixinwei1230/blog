package me.qyh.oauth2;

import me.qyh.oauth2.entity.OauthType;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.security.OauthPrincipal;

/**
 * @see http://wiki.open.qq.com/wiki/website/%E4%BD%BF%E7%94%
 *      A8Authorization_Code%E8%8E%B7%E5%8F%96Access_Token#Step1.EF.BC.9A.E8.8E.
 *      B7.E5.8F.96Authorization_Code 这里只需要获取openid即可
 */
public interface Oauth2 {

	/**
	 * 通过凭证查询用户信息
	 * @param principal
	 * @return
	 */
	OauthUser queryUserInfo(OauthPrincipal principal);

	/**
	 * 用户授权路径
	 * @param state
	 * @return
	 */
	String getAuthorizeUrl(String state);
	
	/**
	 * 获取用户凭证
	 * @param code
	 * @return
	 */
	OauthPrincipal getOauthPrincipal(String code);
	
	OauthType getType();
	
	String callBackUrl();

}
