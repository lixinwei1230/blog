package me.qyh.config;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadConfig {

	private long maxSizeOfUser;
	private _Config config;
	private _ImageConfig imageConfig;

	public long getMaxSizeOfUser() {
		return maxSizeOfUser;
	}

	public void setMaxSizeOfUser(long maxSizeOfUser) {
		this.maxSizeOfUser = maxSizeOfUser;
	}

	public FileUploadConfig(long maxSizeOfUser, _Config config, _ImageConfig imageConfig) {
		super();
		this.maxSizeOfUser = maxSizeOfUser;
		this.config = config;
		this.imageConfig = imageConfig;
	}

	public _Config getConfig() {
		return config;
	}

	public _ImageConfig getImageConfig() {
		return imageConfig;
	}

	public static class _Config {
		private SizeLimit sizeLimit;
		private String[] allowFileTypes;
		private int maxSizeOfQueue;

		public SizeLimit getSizeLimit() {
			return sizeLimit;
		}

		public void setSizeLimit(SizeLimit sizeLimit) {
			this.sizeLimit = sizeLimit;
		}

		public String[] getAllowFileTypes() {
			return allowFileTypes;
		}

		public int getMaxSizeOfQueue() {
			return maxSizeOfQueue;
		}

		public _Config(SizeLimit sizeLimit, String[] allowFileTypes, int maxSizeOfQueue) {
			this.sizeLimit = sizeLimit;
			this.allowFileTypes = allowFileTypes;
			this.maxSizeOfQueue = maxSizeOfQueue;
		}
	}

	public static class _ImageConfig extends _Config {
		private Integer maxWidth;
		private Integer maxHeight;

		public Integer getMaxWidth() {
			return maxWidth;
		}

		public Integer getMaxHeight() {
			return maxHeight;
		}

		public _ImageConfig(SizeLimit sizeLimit, String[] allowExtensions, int maxSizeOfQueue, Integer maxWidth,
				Integer maxHeight) {
			super(sizeLimit, allowExtensions, maxSizeOfQueue);
			this.maxWidth = maxWidth;
			this.maxHeight = maxHeight;
		}
	}

	public interface SizeLimit {
		Result allow(MultipartFile file);

		public static final class Result {
			private boolean allow;
			private long maxAllowSize;

			public Result(boolean allow, long maxAllowSize) {
				this.allow = allow;
				this.maxAllowSize = maxAllowSize;
			}

			public boolean isAllow() {
				return allow;
			}

			public long getMaxAllowSize() {
				return maxAllowSize;
			}
		}
	}

}
