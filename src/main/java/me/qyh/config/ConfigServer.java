package me.qyh.config;

import me.qyh.config.FileUploadConfig._ImageConfig;
import me.qyh.entity.User;

/**
 * 用来获取用户/系统的配置
 * 
 * @author mhlx
 *
 */
public interface ConfigServer {

	/**
	 * 获取用户的附件上传配置
	 * 
	 * @param space
	 *            {@code Space}
	 * @return {@code AttachmentConfig}
	 */
	FileUploadConfig getFileUploadConfig(User user);

	/**
	 * 根据用户获取自定义页面配置
	 * 
	 * @param user
	 * @return
	 */
	PageConfig getPageConfig(User user);

	/**
	 * 获取关于用户的头像配置
	 * 
	 * @param user
	 * @return
	 */
	_ImageConfig getAvatarConfig(User user);

	BlogConfig getBlogConfig(User user);

	FileWriteConfig getFileWriteConfig();
	
	FileWriteConfig getAvatarWriteConfig();
	
	BlogCommentConfig getBlogCommentConfig();

}
