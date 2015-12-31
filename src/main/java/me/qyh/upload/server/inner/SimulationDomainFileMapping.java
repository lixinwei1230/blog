package me.qyh.upload.server.inner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.entity.MyFile;
import me.qyh.exception.SystemException;
import me.qyh.upload.server.FileStorage;
import me.qyh.utils.Files;
import me.qyh.utils.Strings;
import me.qyh.web.tag.url.UrlHelper;

public class SimulationDomainFileMapping implements FileMapping,InitializingBean {
	
	private String prefix;
	private String domain;
	private int count;
	
	@Autowired
	private UrlHelper urlHelper;

	@Override
	public String mapping(MyFile file, FileStorage store) {
		StringBuilder sb = new StringBuilder();
		sb.append(urlHelper.getProtocol()).append("://");
		sb.append(store.id());
		sb.append(domain);
		sb.append("/file");
		sb.append(Strings.cleanPath(file.getRelativePath()));
		sb.append(Files.getFilename(file.getName())).append("/");
		sb.append(Files.getFileExtension(file.getName()));
		return sb.toString();
	}
	
	public boolean match(HttpServletRequest req){
		String servletPath = req.getServletPath();
		if(!servletPath.startsWith("/file")){
			return false;
		}
		String serverName = req.getServerName();
		if(Strings.countOccurrencesOf(serverName, ".") == count){
			return serverName.endsWith(domain);
		}
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String rootDomain = urlHelper.getRootDomain();
		if(rootDomain == null){
			throw new SystemException("配置"+this.getClass().getName()+"失败");
		}
		this.domain = "."+prefix+"."+rootDomain;
		this.count = Strings.countOccurrencesOf(domain, ".");
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
