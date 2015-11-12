package me.qyh.upload.server.inner;

import me.qyh.upload.server.FileStore;
import me.qyh.web.tag.url.UrlHelper;

import org.springframework.beans.factory.annotation.Autowired;

public class InnerFileStore implements FileStore {

	private String uploadRequestUrl;
	private String writeRequestUrl;
	private Integer id;
	private String protocal;

	@Autowired
	private UrlHelper urlHelper;

	/**
	 * 这边文件上传时会有跨域的问题，但是这里仅仅是模仿使用，所以这里只做相对连接
	 */
	@Override
	public String uploadUrl() {
		return urlHelper.getContextPath() + "/" + uploadRequestUrl;
	}

	@Override
	public String seekPrefix() {
		return protocal + "://" + urlHelper.getDomainAndPort() + "/"
				+ writeRequestUrl;
	}

	@Override
	public Integer id() {
		return id;
	}

	public void setUploadRequestUrl(String uploadRequestUrl) {
		this.uploadRequestUrl = uploadRequestUrl;
	}

	public void setWriteRequestUrl(String writeRequestUrl) {
		this.writeRequestUrl = writeRequestUrl;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}
}
