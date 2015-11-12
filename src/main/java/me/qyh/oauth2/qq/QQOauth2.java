package me.qyh.oauth2.qq;

import javax.servlet.http.HttpServletRequest;

import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.security.OauthPrincipal;

/**
 * @see http://wiki.open.qq.com/wiki/website/%E4%BD%BF%E7%94%
 *      A8Authorization_Code%E8%8E%B7%E5%8F%96Access_Token#Step1.EF.BC.9A.E8.8E.
 *      B7.E5.8F.96Authorization_Code 这里只需要获取openid即可
 */
public interface QQOauth2 {

	String getAuthorizeUrl(String state, HttpServletRequest request);

	OpenId getOpenId(AccessToken token);

	AccessToken getAccessToken(String code);

	OauthUser queryUserInfo(OauthPrincipal principal);

}
