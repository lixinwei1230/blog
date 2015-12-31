package me.qyh.web.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import me.qyh.exception.SystemException;
import me.qyh.upload.server.inner.SimulationDomainFileMapping;
import me.qyh.utils.Strings;

public class SimulationDomainFileMappingFilter extends OncePerRequestFilter {
	
	private Collection<SimulationDomainFileMapping> mappings = null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		for(SimulationDomainFileMapping mapping : mappings){
			if(mapping.match(request)){
				String serverName = request.getServerName();
				String storeId = serverName.substring(0, serverName.indexOf("."));
				String path = "/file/"+storeId + Strings.delete(request.getServletPath(), "/file");
				request.getRequestDispatcher(path).forward(request, response);
				return ;
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(super.getServletContext());
		mappings = ctx.getBeansOfType(SimulationDomainFileMapping.class)
				.values();
		if (mappings.isEmpty()) {
			throw new SystemException(
					"无法配置" + this.getClass().getName() + "，需要至少配置一个" + SimulationDomainFileMapping.class.getName());
		}
	}

}
