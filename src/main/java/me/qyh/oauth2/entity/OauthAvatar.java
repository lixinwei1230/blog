package me.qyh.oauth2.entity;

import java.io.Serializable;

public class OauthAvatar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String contentType;
	private transient byte[] bits;
	private int size;
	private String extension;
	private String url;

	/*
	 * public ToStoreFile connect() throws IOException, BadImageException { URL
	 * url = new URL(this.url); URLConnection conn = url.openConnection(); try {
	 * contentType = conn.getContentType(); bits = IOUtils.toByteArray(conn); if
	 * (!Validators.isEmptyOrNull(contentType) &&
	 * contentType.startsWith("image")) { size = bits.length; extension =
	 * contentType.substring("image/".length()); } else { throw new
	 * BadImageException(String.format("从%s获取到的contentType:%s不是属于图片", this.url,
	 * contentType)); } } finally { IOUtils.close(conn); } return
	 * buildToStoreFile(); }
	 */

	public OauthAvatar(String _url) {
		this.url = _url;
	}

	public String getContentType() {
		return contentType;
	}

	public byte[] getBits() {
		return bits;
	}

	public int getSize() {
		return size;
	}

	public String getExtension() {
		return extension;
	}

	public String getUrl() {
		return url;
	}
}
