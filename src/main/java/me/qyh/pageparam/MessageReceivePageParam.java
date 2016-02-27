package me.qyh.pageparam;

import java.util.Date;

import me.qyh.entity.User;
import me.qyh.entity.message.MessageReceive.MessageStatus;
import me.qyh.entity.message.MessageSend;

public class MessageReceivePageParam extends PageParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MessageSend send;
	private MessageStatus status;
	private Boolean isRead;
	private User receiver;
	private Date begin;
	private Date end;

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public MessageSend getSend() {
		return send;
	}

	public void setSend(MessageSend send) {
		this.send = send;
	}

}
