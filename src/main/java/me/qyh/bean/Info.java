package me.qyh.bean;

import org.springframework.web.util.HtmlUtils;

/**
 * 用于json信息的返回
 * 
 * @author mhlx
 *
 */
public class Info implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean success;// 是否成功
	private Object result;// 返回信息

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
		if (result != null && result instanceof String) {
			result = HtmlUtils.htmlEscape((String) result);
		}
	}

	public Info() {

	}

	public Info(boolean success) {
		this.success = success;
	}

	public Info(boolean success, Object result) {
		this.success = success;
		setResult(result);
	}

}
