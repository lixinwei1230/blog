package me.qyh.helper.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;

import me.qyh.web.tag.url.UrlHelper;

@Component
public class NofollowHtmlContentHandler implements BodyFragmentHandler, HtmlContentHandler, InitializingBean {

	private static final String NOFOLLOW = "external nofollow";

	private String siteDomain;

	@Autowired
	private UrlHelper urlHelper;

	@Override
	public String handle(String htmlContent) {

		Document doc = Jsoup.parseBodyFragment(htmlContent);
		Element body = doc.body();
		Elements eles = body.select("a[href]");
		for (Element ele : eles) {
			String href = ele.attr("href");
			// only abs url need to do
			if (needNofollow(href)) {
				ele.attr("rel", NOFOLLOW);
			}
		}
		return body.html();
	}

	@Override
	public void handle(Document body) {
		Elements eles = body.select("a[href]");
		for (Element ele : eles) {
			String href = ele.attr("href");
			// only abs url need to do
			if (needNofollow(href)) {
				ele.attr("rel", NOFOLLOW);
			}
		}
	}

	protected boolean needNofollow(String href) {
		return (UrlUtils.isAbsoluteUrl(href) && href.indexOf(siteDomain) == -1);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String domain = urlHelper.getDomain();
		if (domain.indexOf(".") > 0) {
			String[] splited = domain.split("\\.");
			int length = splited.length;
			siteDomain = splited[length - 2] + "." + splited[length - 1];
		} else {
			siteDomain = domain;
		}
	}
}
