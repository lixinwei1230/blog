package me.qyh.web;

/**
 * 用来封装ajax提交表单时候，validator验证未通过时候返回的错误信息
 * 
 * @author mhlx
 *
 */
public class MyFieldError {

	private String field;// 错误属性名
	private String error;// 错误信息

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public MyFieldError() {
		super();
	}

	public MyFieldError(String field, String error) {
		super();
		this.field = field;
		this.error = error;
	}

}
