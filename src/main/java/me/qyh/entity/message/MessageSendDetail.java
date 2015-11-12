package me.qyh.entity.message;

import java.util.HashSet;
import java.util.Set;

/**
 * 信息发送明细
 * 
 * @author mhlx
 *
 */
public class MessageSendDetail extends MessageSend {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Set<String> receivers = new HashSet<String>();

	public Set<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(Set<String> receivers) {
		this.receivers = receivers;
	}

}
