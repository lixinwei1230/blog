package me.qyh.entity.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.qyh.entity.Id;
import me.qyh.helper.htmlclean.JsonHtmlXssSerializer;

/**
 * 信息明细
 * 
 * @author mhlx
 *
 */
public class MessageDetail extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String title;// 标题
	private String content;// 内容

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MessageDetail(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public MessageDetail() {

	}

	public MessageDetail(Integer id) {
		super(id);
	}

}
