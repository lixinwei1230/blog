package me.qyh.config;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class FileWriteConfig {

	private RequestMatcher requestMatcher;
	private ImageZoomMatcher zoomMatcher;

	public RequestMatcher getRequestMatcher() {
		return requestMatcher;
	}

	public void setRequestMatcher(RequestMatcher requestMatcher) {
		this.requestMatcher = requestMatcher;
	}

	public ImageZoomMatcher getZoomMatcher() {
		return zoomMatcher;
	}

	public void setZoomMatcher(ImageZoomMatcher zoomMatcher) {
		this.zoomMatcher = zoomMatcher;
	}

	FileWriteConfig(RequestMatcher requestMatcher,
			ImageZoomMatcher zoomMatcher) {
		super();
		this.requestMatcher = requestMatcher;
		this.zoomMatcher = zoomMatcher;
	}

}
