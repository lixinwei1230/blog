
package me.qyh.web.tag.url;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 用来获取一些用户、用户主页的url
 * 
 * @author mhlx
 *
 */
public class ResizeTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer size;
	private String url;

	@Override
	public int doStartTag() throws JspException {

		JspWriter out = this.pageContext.getOut();
		try {
			out.write(ResizeTagHelpers.getUrl(url, size));
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}