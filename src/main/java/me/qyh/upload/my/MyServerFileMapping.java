package me.qyh.upload.my;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.entity.MyFile;
import me.qyh.exception.SystemException;
import me.qyh.upload.server.FileStorage;
import me.qyh.upload.server.inner.FileMapping;
import me.qyh.utils.Strings;
import me.qyh.web.tag.url.UrlHelper;

public class MyServerFileMapping implements FileMapping , InitializingBean{
	
	private String prefix;
	private String domain;
	
	@Autowired
	private UrlHelper urlHelper;

	@Override
	public String mapping(MyFile file, FileStorage store) {
		StringBuilder sb = new StringBuilder(prefix(store));
		sb.append(Strings.cleanPath(file.getRelativePath()));
		sb.append(file.getName());
		return sb.toString();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		String rootDomain = urlHelper.getRootDomain();
		if(rootDomain == null){
			throw new SystemException("配置"+this.getClass().getName()+"失败");
		}
		this.domain = "."+prefix+"."+rootDomain;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String prefix(FileStorage store){
		StringBuilder sb = new StringBuilder();
		sb.append(urlHelper.getProtocol()).append("://");
		sb.append(store.id());
		sb.append(domain);
		return sb.toString();
	}
}
