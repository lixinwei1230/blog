package me.qyh.upload.server;

public interface FileStore {

	/**
	 * 上传路径
	 * 
	 * @return
	 */
	String uploadUrl();

	/**
	 * 将路径发送给存储器 路径有前缀+相对路径组成
	 * 
	 * @return
	 */
	String seekPrefix();

	/**
	 * 获取存储器的ID
	 * 
	 * @return
	 */
	Integer id();
	
	String deleteUrl();
	
	String delKey();

}
