package me.qyh.helper.file;

import java.io.File;

import me.qyh.bean.Crop;

public interface ImageProcessing {
	
	/**
	 * 缩放图片
	 * @param src
	 * @param dest
	 * @param size
	 */
	void zoom(File src,File dest,Resize size) throws Exception;
	
	/**
	 * 裁剪图片
	 * @param crop
	 * @param dest
	 * @throws Exception 
	 */
	void crop(Crop crop,File dest) throws Exception;
	
	/**
	 * 重写文件格式
	 * @param src
	 * @param desc
	 * @param format
	 */
	void format(File src,File desc,String format) throws Exception;
	
	/**
	 * 读取图片信息
	 * @param src
	 * @return
	 */
	ImageInfo read(File src) throws BadImageException;
	
	/**
	 * 
	 * @param gif
	 * @return
	 * @throws Exception
	 */
	int getFrameNumsOfGif(File gif) throws Exception;
	
	/**
	 * 
	 * @param gif
	 * @param desc
	 * @throws Exception 
	 */
	void writeFirstFrameOfGif(File gif,File desc) throws Exception;
	
	
	void strip(File src , File desc) throws Exception;
}
