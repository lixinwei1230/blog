package me.qyh.entity.message;

import me.qyh.entity.Id;
import me.qyh.entity.User;

/**
 * 信息接收对象
 * 
 * @author mhlx
 *
 */
public class MessageReceive extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User receiver;// 接收人
	private MessageSend message;// 信息来源
	private boolean isRead;// 是否阅读
	private MessageStatus status;// 信息状态

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public MessageSend getMessage() {
		return message;
	}

	public void setMessage(MessageSend message) {
		this.message = message;
	}

	public boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

}
