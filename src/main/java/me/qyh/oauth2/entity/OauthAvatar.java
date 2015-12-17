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
