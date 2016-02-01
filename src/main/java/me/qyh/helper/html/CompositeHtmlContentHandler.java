package me.qyh.helper.html;

import java.util.ArrayList;
import java.util.List;

public class CompositeHtmlContentHandler implements HtmlContentHandler {

	private List<HtmlContentHandler> handlers = new ArrayList<HtmlContentHandler>();

	@Override
	public String handle(String htmlContent) {
		String toHandle = htmlContent;
		if (!handlers.isEmpty()) {
			for (HtmlContentHandler handler : handlers) {
				toHandle = handler.handle(toHandle);
			}
		}
		return toHandle;
	}

	public void setHandlers(List<HtmlContentHandler> handlers) {
		this.handlers = handlers;
	}

}
