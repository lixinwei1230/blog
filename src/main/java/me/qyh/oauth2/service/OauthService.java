package me.qyh.oauth2.service;

import java.util.List;

import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.oauth2.entity.OauthType;
import me.qyh.oauth2.entity.OauthUser;
import me.qyh.oauth2.security.OauthPrincipal;

public interface OauthService {

	/**
	 * 如果用户没有已经存在账号，那么自动添加一个绑定账号
	 * 
	 * @param oauthUser
	 * @return
	 * @throws LogicException
	 */
	User autoBind(OauthPrincipal principal, OauthUser oauthUser)
			throws LogicException;

	/**
	 * 将授权用户和已经存在账号绑定
	 */
	User bind(OauthPrincipal principal, String code, String email,
			OauthUser oauthUser) throws LogicException;

	/**
	 * 根据当前凭证查找用户是否已经绑定
	 * 
	 * @param principal
	 * @return
	 */
	User get(OauthPrincipal principal);

	/**
	 * 发送绑定邮件
	 * 
	 * @param principal
	 * @throws LogicException
	 */
	void sendBindEmail(OauthPrincipal principal, String email)
			throws LogicException;

	/**
	 * 解绑
	 * 
	 * @param user
	 *            需要解绑的用户
	 * @throws LogicException
	 */
	void unbind(User user, OauthType type) throws LogicException;

	/**
	 * 查询用户所有的绑定
	 * 
	 * @param user
	 * @return
	 */
	List<OauthUser> findOauthBinds(User user);

	void completeInfo(User user, int userid, String code) throws LogicException;

	void sendCompleteInfoEmail(String code, User user) throws LogicException;

	void checkCompleteInfoEmail(String code, int userid) throws LogicException;

	void sendAuthorizeEmail(String email, User user) throws LogicException;

}
