package me.qyh.web;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.SessionCookieConfig;

import org.springframework.web.context.ContextLoaderListener;

import me.qyh.web.filter.SpaceDomainFilter;
import me.qyh.web.tag.url.UrlHelper;

public class MyContextLoaderListener extends ContextLoaderListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);

		UrlHelper helper = super.getCurrentWebApplicationContext()
				.getBean(UrlHelper.class);

		if (helper.isEnableSpaceDomain()) {
			// 如果开启space域名，则增加SpaceDomainFilter
			ServletContext sc = event.getServletContext();
			Class<? extends Filter> spaceDomainFilter = SpaceDomainFilter.class;
			sc.addFilter(spaceDomainFilter.getName(), spaceDomainFilter)
					.addMappingForUrlPatterns(
							EnumSet.of(DispatcherType.REQUEST), true, "/*");

			String domain = helper.getDomain();
			if (domain.startsWith("www.")) {
				domain = domain.substring(domain.indexOf("."));
			} else {
				domain = "." + domain;
			}
			SessionCookieConfig config = sc.getSessionCookieConfig();
			config.setDomain(domain);
		}
	}

}
