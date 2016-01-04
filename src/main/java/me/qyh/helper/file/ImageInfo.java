package me.qyh.helper.file;

public class ImageInfo {
	private int width;
	private int height;
	private String type;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getType() {
		return type;
	}

	public ImageInfo(int width, int height, String type) {
		this.width = width;
		this.height = height;
		this.type = type;
	}

	@Override
	public String toString() {
		return "ImageInfo [width=" + width + ", height=" + height + ", type="
				+ type + "]";
	}
}