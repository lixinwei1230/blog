package me.qyh.entity;

public enum UserCodeType {

	ACTIVATE, // 激活
	FORGETPASSWORD, // 忘记密码
	OAUTH_BIND, // 绑定
	OAUTH_AUTHORIZE_EMAIIL, // 确认用户提供的email属于该用户
	OAUTH_COMPLETE_USERINFO// 完善用户信息
}
