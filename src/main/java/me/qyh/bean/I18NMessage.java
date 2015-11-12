package me.qyh.bean;

public class I18NMessage {

	private String code;
	private Object[] params;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public I18NMessage(String code, Object... params) {
		this.code = code;
		this.params = params;
	}

}
