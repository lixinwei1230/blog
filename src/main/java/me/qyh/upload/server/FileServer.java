package me.qyh.upload.server;

/**
 * 文件服务器
 * 
 * @author mhlx
 *
 */
public interface FileServer {

	/**
	 * 根据ID查找存储器
	 * 
	 * @param id
	 *            存储器ID
	 * @return {@code FileStore}
	 */
	FileStore getStore(int id);

	FileStore getStore();

}
