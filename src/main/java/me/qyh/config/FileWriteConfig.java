package me.qyh.config;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class FileWriteConfig {

	private RequestMatcher requestMatcher;
	private ImageZoomMatcher zoomMatcher;

	public RequestMatcher getRequestMatcher() {
		return requestMatcher;
	}

	public ImageZoomMatcher getZoomMatcher() {
		return zoomMatcher;
	}

	FileWriteConfig(RequestMatcher requestMatcher, ImageZoomMatcher zoomMatcher) {
		super();
		this.requestMatcher = requestMatcher;
		this.zoomMatcher = zoomMatcher;
	}

}
