package me.qyh.helper.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BootstrapHtmlHandler implements BodyFragmentHandler,
		HtmlContentHandler {

	private static final String IMG_RESPONSIVE = "img-responsive";
	private static final String NO_RESPONSIVE = "no-responsive";
	private static final String TABLE_RESPONSIVE = "table-responsive";
	private static final String EMBED_RESPONSIVE = "embed-responsive";
	private static final String EMBED_RESPONSIVE_SIZE = "embed-responsive-16by9";
	private static final String EMBED_RESPONSIVE_ITEM = "embed-responsive-item";
	// <iframe>、<embed>、<video> 和 <object>
	private static final String[] EMBEDS = new String[] { "iframe", "embed",
			"video", "object" };

	private String imgResponsive = IMG_RESPONSIVE;
	private String noResponsive = NO_RESPONSIVE;
	private String tableResponsive = TABLE_RESPONSIVE;
	private String embedResponsive = EMBED_RESPONSIVE;
	private String embedResponsiveSize = EMBED_RESPONSIVE_SIZE;
	private String embedResponsiveItem = EMBED_RESPONSIVE_ITEM;

	@Override
	public String handle(String htmlContent) {
		Document doc = Jsoup.parseBodyFragment(htmlContent);
		handle(doc);
		return doc.html();
	}

	@Override
	public void handle(Document body) {
		Elements imgs = body.select("img[src]");
		if (!imgs.isEmpty()) {
			for (Element img : imgs) {
				if (!noResponsive(img) && !img.hasClass(imgResponsive)) {
					img.addClass(imgResponsive);
				}
			}
		}
		Elements tables = body.getElementsByTag("table");
		if (!tables.isEmpty()) {
			for (Element table : tables) {
				if (!noResponsive(table)) {
					Element parent = table.parent();
					if (parent == null) {
						wrapTableForResponsive(table);
					} else {
						if ("div".equalsIgnoreCase(parent.tagName())) {
							if (!parent.hasClass(tableResponsive)) {
								parent.addClass(tableResponsive);
							}
						} else {
							wrapTableForResponsive(table);
						}
					}
				}
			}
		}
		for (String embed : EMBEDS) {
			Elements embeds = body.getElementsByTag(embed);
			if (!embeds.isEmpty()) {
				for (Element ed : embeds) {
					if (!noResponsive(ed)) {
						if(embedResponsiveItem != null && !ed.hasClass(embedResponsiveItem)){
							ed.addClass(embedResponsiveItem);
						}
						
						Element parent = ed.parent();
						if (parent == null) {
							wrapEmbedForResponsive(ed);
						} else {
							if ("div".equalsIgnoreCase(parent.tagName())) {
								if(!parent.hasClass(embedResponsive)){
									parent.addClass(embedResponsive);
								}
								if(!parent.hasClass(embedResponsiveSize)){
									parent.addClass(embedResponsiveSize);
								}
							} else {
								wrapEmbedForResponsive(ed);
							}
						}
					}
				}
			}
		}
	}

	private boolean noResponsive(Element ele) {
		return noResponsive != null && ele.hasClass(noResponsive);
	}

	private void wrapTableForResponsive(Element table) {
		table.wrap("<div class='" + tableResponsive + "'></div>");
	}

	private void wrapEmbedForResponsive(Element table) {
		table.wrap("<div class='" + embedResponsive + " " + embedResponsiveSize
				+ "'></div>");
	}

	public void setImgResponsive(String imgResponsive) {
		this.imgResponsive = imgResponsive;
	}

	public void setNoResponsive(String noResponsive) {
		this.noResponsive = noResponsive;
	}

	public void setTableResponsive(String tableResponsive) {
		this.tableResponsive = tableResponsive;
	}

	public void setEmbedResponsive(String embedResponsive) {
		this.embedResponsive = embedResponsive;
	}

	public void setEmbedResponsiveSize(String embedResponsiveSize) {
		this.embedResponsiveSize = embedResponsiveSize;
	}
}
