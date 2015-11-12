package me.qyh.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.security.UserContext;
import me.qyh.utils.Strings;
import me.qyh.web.Webs;
import me.qyh.web.tag.url.UrlHelper;

public class SpaceDomainFilter extends OncePerRequestFilter {

	private static final String[] ignorePathPatterns = { "/avatar/*", "/my/**",
			"/captcha/*", "/favicon.ico",// web
											// ico
	};

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
					throws ServletException, IOException {
		String uri = getUriCleanedWithoutContextPath(request.getRequestURI(),
				request.getContextPath());

		PathMatcher pathMatcher = new AntPathMatcher();
		// 静态资源和ajax请求直接放行
		if (pathMatcher.match("/static/**", uri)
				|| Webs.isAjaxRequest(request)) {
			chain.doFilter(request, response);
			return;
		}

		ServletContext sc = request.getServletContext();

		UrlHelper helper = Webs.getUrlHelper(sc);

		if (maybeSpaceUrl(uri)) {
			String space = getSpaceFromUri(uri);
			response.sendRedirect(request.getScheme() + "://"
					+ buildSpaceUrlWithPort(helper, space));
			return;
		}

		String domain = request.getServerName();
		// 如果是spaceDomain
		boolean maybeSpaceDomain = maybeSpaceDomain(helper.getDomain(), domain);
		if (maybeSpaceDomain) {
			String space = getSpaceFromDomain(domain);
			// 比如XXXX.qyh.com/my/index XXXX并非当前登录用户的空间名
			if (pathMatcher.match("/my/**", uri)) {
				User current = UserContext.getUser();
				// /my/会被SpringSecurity做权限判断，所以这里current必然会存在
				Space _space = current.getSpace();
				if (_space == null || !_space.getId().equals(space)) {
					// 跳转到用户主页
					String spaceUrl = request.getScheme() + "://"
							+ helper.getUrlByUser(current, true) + "/index";
					response.sendRedirect(spaceUrl);
					return;
				}
			}

			// 链接需要转发
			if (isForwardUri(pathMatcher, uri)) {
				RequestDispatcher rd = request
						.getRequestDispatcher("/space/" + space + uri);
				rd.forward(request, response);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private boolean isForwardUri(PathMatcher pathMatcher, String cleanedUri) {
		for (String ignorePathPattern : ignorePathPatterns) {
			if (pathMatcher.match(ignorePathPattern, cleanedUri)) {
				return false;
			}
		}
		return true;
	}

	private String buildSpaceUrlWithPort(UrlHelper helper, String space) {
		String domainAndPort = helper.getDomainAndPort();
		String spaceDomainAndPort = "";
		if (domainAndPort.startsWith("www.")) {
			spaceDomainAndPort = domainAndPort.replace("www", space);
		} else {
			spaceDomainAndPort = space + "." + domainAndPort;
		}
		return spaceDomainAndPort + helper.getContextPath();
	}

	private boolean maybeSpaceUrl(String uri) {
		return Strings.startsWithIgnoreCase(uri, "/space/");
	}

	private boolean maybeSpaceDomain(String configuredDomain, String domain) {
		if (domain.startsWith("www.")) {
			return false;
		}
		if (domain.equalsIgnoreCase(configuredDomain)) {
			return false;
		}
		// qyh.me
		int index = domain.indexOf('.');
		if (index != -1) {
			String _domain = domain.substring(index + 1);
			return _domain.equalsIgnoreCase(configuredDomain)
					|| ("www." + _domain).equalsIgnoreCase(configuredDomain);
		}
		return false;
	}

	private String getSpaceFromUri(String uri) {
		String space = "";
		String _uri = uri.split("/space/")[1];

		if (_uri.indexOf("/") == -1) {
			space = _uri;
		} else {
			space = _uri.split("/")[0];
		}

		return space;
	}

	private String getSpaceFromDomain(String spaceDomain) {
		return spaceDomain.substring(0, spaceDomain.indexOf('.'));
	}

	private String getUriCleanedWithoutContextPath(String uri,
			String contextPath) {
		String _uri = Strings.cleanPath(uri);
		char chars[] = _uri.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char c : chars) {
			if (c == '/') {
				if (sb.length() == 0) {
					sb.append(c);
				} else if (sb.charAt(sb.length() - 1) != '/') {
					sb.append("/");
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString().replaceFirst(contextPath, "");
	}

}
