package me.qyh.server;

import java.io.Serializable;

import me.qyh.entity.User;

public class TipMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User sender;
	private User receiver;
	private String title;
	private String content;

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

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

}
