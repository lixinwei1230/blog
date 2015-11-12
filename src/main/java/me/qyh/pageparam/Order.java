package me.qyh.pageparam;

import java.io.Serializable;

public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String property;
	private OrderType type;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public Order(String property) {
		this.property = property;
	}

	public Order(String property, OrderType type) {
		this.property = property;
		this.type = type;
	}

}
