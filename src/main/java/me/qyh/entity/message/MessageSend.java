package me.qyh.entity.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.qyh.entity.Id;
import me.qyh.entity.User;

/**
 * 信息来源
 * 
 * @author mhlx
 *
 */
public class MessageSend extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User sender;// 发送人
	private Date sendDate;// 发送日期
	private boolean isDeleted;// 是否删除
	private MessageDetail detail;// 信息详细
	private MessageType type;// 信息类型
	private List<MessageReceive> receives = new ArrayList<MessageReceive>();// 信息接收人
	
	public enum MessageType {
		PERSONAL, // 个人
		GLOBAL, // 全局
		SYSTEM// 系统
	}

	public MessageSend(Integer id) {
		super(id);
	}

	public MessageSend() {

	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public MessageDetail getDetail() {
		return detail;
	}

	public void setDetail(MessageDetail detail) {
		this.detail = detail;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public List<MessageReceive> getReceives() {
		return receives;
	}

	public void setReceives(List<MessageReceive> receives) {
		this.receives = receives;
	}
}
