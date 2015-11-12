package me.qyh.web.tag.token;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 用来防止用户重复提交
 * 
 * @author mhlx
 *
 */
public class TokenTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {

		JspWriter out = this.pageContext.getOut();
		try {
			String tokenName = TokenHelper.generateGUID();
			out.print(
					"<input type='hidden' name='" + TokenHelper.TOKEN_NAME_FIELD
							+ "' value='" + tokenName + "'/>");
			String token = TokenHelper.setToken(
					(HttpServletRequest) this.pageContext.getRequest(),
					tokenName);
			out.print("<input type='hidden' name='" + tokenName + "' value='"
					+ token + "'/>");
		} catch (IOException e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;

	}

}