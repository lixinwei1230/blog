package me.qyh.web.tag.url;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.web.Webs;

/**
 * 用来获取一些用户、用户主页的url
 * 
 * @author mhlx
 *
 */
public class UrlTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private Space space;
	private boolean myMenu;
	private String var;
	private String protocal;

	@Override
	public int doStartTag() throws JspException {

		JspWriter out = this.pageContext.getOut();
		try {
			PageContext pc = this.pageContext;
			ServletContext sc = pc.getServletContext();
			UrlHelper helper = Webs.getUrlHelper(sc);
			String protocal = this.protocal == null ? helper.getProtocal() : this.protocal;
			String url = "";
			if (user != null) {
				url = helper.getUrlByUser(user, myMenu , protocal);
			} else if (space != null) {
				url = helper.getUrlBySpace(space , protocal);
			} else {
				url = helper.getUrl( protocal);
			}
			if (var != null) {
				pageContext.setAttribute(var, url);
			} else {
				out.write(url);
			}
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public void setMyMenu(boolean myMenu) {
		this.myMenu = myMenu;
	}

	public void setVar(String var) {
		this.var = var;
	}

}