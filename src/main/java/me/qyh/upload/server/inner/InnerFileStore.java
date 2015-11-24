package me.qyh.upload.server.inner;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.upload.server.FileStore;
import me.qyh.utils.Strings;
import me.qyh.web.tag.url.UrlHelper;

public class InnerFileStore implements FileStore , InitializingBean {

	private String uploadRequestUrl;
	private String writeRequestUrl;
	private Integer id;
	private String deleteRequestUrl;
	private String delKey;

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
		return urlHelper.getProtocal() + "://" + urlHelper.getDomainAndPort() + "/" + writeRequestUrl;
	}

	@Override
	public String deleteUrl() {
		return urlHelper.getProtocal() + "://" + urlHelper.getDomainAndPort() + "/" + deleteRequestUrl;
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

	public void setDeleteRequestUrl(String deleteRequestUrl) {
		this.deleteRequestUrl = deleteRequestUrl;
	}

	@Override
	public String delKey() {
		return delKey;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		delKey = Strings.getMd5(Strings.getMd5(id + deleteRequestUrl));
	}
}
