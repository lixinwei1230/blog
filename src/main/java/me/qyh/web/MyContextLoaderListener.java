package me.qyh.web;

import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.SessionCookieConfig;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import me.qyh.upload.server.inner.LocalFileStorage;
import me.qyh.upload.server.inner.SimulationDomainFileMapping;
import me.qyh.utils.Validators;
import me.qyh.web.filter.SimulationDomainFileMappingFilter;
import me.qyh.web.filter.SpaceDomainFilter;
import me.qyh.web.tag.url.UrlHelper;

public class MyContextLoaderListener extends ContextLoaderListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		WebApplicationContext ctx = super.getCurrentWebApplicationContext();
		UrlHelper helper = ctx.getBean(UrlHelper.class);
		String domain = helper.getDomain();
		ServletContext sc = event.getServletContext();
		Map<String, LocalFileStorage> storesMap = ctx.getBeansOfType(LocalFileStorage.class);
		if(!storesMap.isEmpty()){
			for(LocalFileStorage store : storesMap.values()){
				if(store.getMapping() instanceof SimulationDomainFileMapping){
					Class<? extends Filter> simulationDomainFileMappingFilter = SimulationDomainFileMappingFilter.class;
					sc.addFilter(simulationDomainFileMappingFilter.getName(), simulationDomainFileMappingFilter)
							.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
				}
				break;
			}
		}
		if (!(Validators.validateIp(domain) || "localhost".equals(domain))) {
			Class<? extends Filter> spaceDomainFilter = SpaceDomainFilter.class;
			sc.addFilter(spaceDomainFilter.getName(), spaceDomainFilter)
					.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		}
		if (domain.startsWith("www.")) {
			domain = domain.substring(domain.indexOf("."));
		} else {
			domain = "." + domain;
		}
		SessionCookieConfig config = sc.getSessionCookieConfig();
		config.setDomain(domain);
	}
}
