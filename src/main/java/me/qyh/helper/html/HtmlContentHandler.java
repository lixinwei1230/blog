package me.qyh.helper.html;

public interface HtmlContentHandler {

	/**
	 * 处理html片段
	 * 
	 * @param htmlContent
	 *            需要处理的html片段
	 * @return 处理完之后的html片段
	 */
	String handle(String htmlContent);

}
