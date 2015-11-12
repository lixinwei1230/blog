package me.qyh.config;

/**
 * 评论配置
 * 
 * @author mhlx
 *
 */
public class CommentConfig {

	private boolean commentOnAuthorOnly;// 只能对评论所有者的评论或者回复进行回复
	private boolean allowHtml;// 评论是否允许html片段，如果允许，将会调用统一的CommentHtmlClean进行处理
	private boolean allowAnonymous;// 允许匿名
	private FrequencyLimit limit;// 频率频率限制

	public boolean getCommentOnAuthorOnly() {
		return commentOnAuthorOnly;
	}

	public void setCommentOnAuthorOnly(boolean commentOnAuthorOnly) {
		this.commentOnAuthorOnly = commentOnAuthorOnly;
	}

	public boolean getAllowHtml() {
		return allowHtml;
	}

	public void setAllowHtml(boolean allowHtml) {
		this.allowHtml = allowHtml;
	}

	public boolean getAllowAnonymous() {
		return allowAnonymous;
	}

	public void setAllowAnonymous(boolean allowAnonymous) {
		this.allowAnonymous = allowAnonymous;
	}

	public FrequencyLimit getLimit() {
		return limit;
	}

	public void setLimit(FrequencyLimit limit) {
		this.limit = limit;
	}

}
