package me.qyh.upload.server.inner;

/**
 * 图片缩放配置
 *
 */
public class ScalrConfig {

	// 图片质量，只针对jpg有效果
	private Float quality = 1F;
	// 图片缩放高度
	private int height;
	// 图片缩放宽度
	private int width;
	// 如果设定了这个值，将会使宽或高适应这个值，如果宽大于高，那么宽为这个值，否则高为这个值
	private Integer size;
	// 图片输出格式
	private String outputFormat;
	// 保持横纵比
	private boolean aspectRatio = true;

	public Float getQuality() {
		return quality;
	}

	public void setQuality(Float quality) {
		this.quality = quality;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public boolean getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(boolean aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public void calc(int imageWidth, int imageHeight) {
		if (size != null) {
			this.aspectRatio = false;
			double sourceRatio = (double) imageWidth / (double) imageHeight;
			if (imageWidth >= imageHeight) {
				this.width = size;
				this.height = ((int) Math.round(size / sourceRatio));
			}
			if (imageWidth < imageHeight) {
				this.height = size;
				this.width = ((int) Math.round(size * sourceRatio));
			}
		}
	}
}
