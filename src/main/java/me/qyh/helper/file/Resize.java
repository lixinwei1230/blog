package me.qyh.helper.file;

/**
 * 文件缩放 如果某一项为0，那么
 * @author mhlx
 *
 */
public class Resize {
	
	private int width;
	private int height;
	private int size;
	private boolean force;
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isForce() {
		return force;
	}
	public void setForce(boolean force) {
		this.force = force;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
