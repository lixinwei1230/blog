package me.qyh.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.security.UserContext;
import me.qyh.utils.Strings;
import me.qyh.web.Webs;
import me.qyh.web.tag.url.UrlHelper;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class SpaceDomainFilter extends OncePerRequestFilter {

	private static final String[] ignorePathPatterns = { "/avatar/*", "/my/**",
			"/captcha/*", "/favicon.ico" };

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String uri = getUriCleanedWithoutContextPath(request.getRequestURI(),
				request.getContextPath());
		PathMatcher pathMatcher = new AntPathMatcher();
		// 静态资源和ajax请求直接放行
		if (pathMatcher.match("/static/**", uri) || Webs.isAjaxRequest(request)) {
			chain.doFilter(request, response);
			return;
		}
		String domain = request.getServerName();
		ServletContext sc = request.getServletContext();

		UrlHelper helper = Webs.getUrlHelper(sc);
		// 如果是spaceDomain
		boolean maybeSpaceDomain = maybeSpaceDomain(helper.getDomain(), domain);
		if (helper.isEnableSpaceDomain()) {
			if (maybeSpaceUrl(uri)) {
				String space = getSpaceFromUri(uri);
				response.sendRedirect(buildSpaceUrlWithPort(helper, space));
				return;
			}
			if (maybeSpaceDomain) {
				String space = getSpaceFromDomain(domain);
				if (pathMatcher.match("/my/**", uri)) {
					User current = UserContext.getUser();
					if(current != null){
						Space _space = current.getSpace();
						if (_space == null || !_space.getId().equals(space)) {
							// 跳转到用户主页
							String spaceUrl = helper.getUrlByUser(current, true)
									+ "/index";
							response.sendRedirect(spaceUrl);
							return;
						}
					}
				}

				// 链接需要转发
				if (isForwardUri(pathMatcher, uri)) {
					RequestDispatcher rd = request
							.getRequestDispatcher("/space/" + space + uri);
					rd.forward(request, response);
					return;
				}
			} else if (!domain.equalsIgnoreCase(helper.getDomain())) {
				String redirectUrl = helper.getUrl() + uri;
				if (pathMatcher.match("/my/**", uri)) {
					Space space = UserContext.getSpace();
					if(space != null){
						redirectUrl = buildSpaceUrlWithPort(helper, space.getId()) + uri;
					}
				}
				response.sendRedirect(redirectUrl);
				return;
			}
		} else if (maybeSpaceDomain) {
			// mhlx.qyh.me == > qyh.me/space/mhlx
			String space = getSpaceFromDomain(domain);
			String url = helper.getUrl() + (isForwardUri(pathMatcher, uri) ? "/space/" + space : "") + uri;
			response.sendRedirect(url);
			return;
		} else if (!domain.equalsIgnoreCase(helper.getDomain())) {
			response.sendRedirect(helper.getUrl() + uri);
			return;
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
		return helper.getProtocal() + "://" + spaceDomainAndPort + helper.getContextPath();
	}

	private boolean maybeSpaceUrl(String uri) {
		return Strings.startsWithIgnoreCase(uri, "/space/");
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

	private boolean maybeSpaceDomain(String configured, String domain) {
		if (domain.startsWith("www.")
				&& Strings.countOccurrencesOf(domain, ".") == 2) {
			return false;
		}
		int pCount = Strings.countOccurrencesOf(configured, ".");
		boolean wwwConfigured = configured.startsWith("www.") && pCount == 2;
		return Strings.countOccurrencesOf(domain, ".") == ((wwwConfigured ? 1
				: pCount) + 1);
	}

}