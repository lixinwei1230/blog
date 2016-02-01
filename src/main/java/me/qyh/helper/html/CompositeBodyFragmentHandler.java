package me.qyh.helper.html;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import me.qyh.utils.Validators;

public class CompositeBodyFragmentHandler implements HtmlContentHandler {
	
	private List<BodyFragmentHandler> handlers ;

	@Override
	public String handle(String htmlContent) {
		String html = htmlContent;
		if(!Validators.isEmptyOrNull(handlers)){
			Document body = Jsoup.parseBodyFragment(htmlContent);
			for(BodyFragmentHandler handler : handlers){
				handler.handle(body);
			}
			html = body.body().html();
		}
		return html;
	}

	public void setHandlers(List<BodyFragmentHandler> handlers) {
		this.handlers = handlers;
	}
}
