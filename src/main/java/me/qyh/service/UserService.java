package me.qyh.service;

import me.qyh.bean.Crop;
import me.qyh.entity.LoginInfo;
import me.qyh.entity.User;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.LoginInfoPageParam;
import me.qyh.pageparam.Page;

public interface UserService {

	void register(User user) throws LogicException;

	void reactive(String name, String mail) throws LogicException;

	void activate(Integer userId, String activateCode) throws LogicException;

	void forgetPassword(String name, String password) throws LogicException;

	void resetPasswordCheck(String code, Integer userId) throws LogicException;

	void resetPassword(String code, String password, Integer userId) throws LogicException;

	void changePassword(String oldPassword, String newPassword) throws LogicException;

	void changeNickname(String nickname) throws LogicException;

	void updateAvatar(Crop crop) throws LogicException;
	
	Page<LoginInfo> findLoginInfos(LoginInfoPageParam param);

}
