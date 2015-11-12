package me.qyh.web.tag.url;

import me.qyh.entity.Space;
import me.qyh.entity.User;

/**
 * 失败的产物...不知道该如何处理SpaceDomain的路径
 * 
 * <br/>
 * 2015-08-23现在为spring bean [id="urlHelper"]
 * 
 * @author mhlx
 *
 */
public interface UrlHelper {

	/**
	 * 
	 * @return
	 */
	String getUrl();

	/**
	 * 
	 * @param user
	 * @param myMenu
	 * @return
	 */
	String getUrlByUser(User user, boolean myMenu);

	/**
	 * 
	 * @param space
	 * @return
	 */
	String getUrlBySpace(Space space);

	/**
	 * 得到当前博客app的域名和端口
	 * 
	 * @return
	 */
	String getDomainAndPort();

	/**
	 * 返回ServletContext getContextPath()的值
	 * 
	 * @return
	 */
	String getContextPath();

	/**
	 * 返回当前网站的域名
	 * 
	 * @return
	 */
	String getDomain();

	boolean isEnableSpaceDomain();

}
