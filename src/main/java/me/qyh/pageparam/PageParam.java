package me.qyh.pageparam;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import me.qyh.web.InvalidParamException;

public abstract class PageParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int currentPage;
	private int pageSize;
	private Set<Order> orders = new LinkedHashSet<Order>();

	public int getOffset() {
		return Page.countOffset(currentPage, pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public PageParam() {
		super();
	}

	public PageParam(int currentPage, int pageSize) {
		super();
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	private void toMap(Map<String, Object> map, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length > 0) {
			for (Field field : fields) {
				String fieldName = field.getName();
				try {
					Method method = clazz.getMethod(buildGetMethod(fieldName));
					map.put(fieldName, method.invoke(this));
				} catch (Exception e) {
					continue;
				}
			}
		}
		if (isSuperClass(clazz, PageParam.class)) {
			toMap(map, clazz.getSuperclass());
		}
	}

	private boolean isSuperClass(Class<?> child, Class<?> target) {
		if (child == null) {
			return false;
		}
		Class<?> superClazz = child.getSuperclass();
		if (superClazz == null) {
			return false;
		}
		if (superClazz.equals(target)) {
			return true;
		}
		return isSuperClass(superClazz, target);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		toMap(map, this.getClass());
		return map;
	}

	private String buildGetMethod(String fieldName) {
		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		return "get".concat(firstLetter).concat(fieldName.substring(1));
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public void validate() throws InvalidParamException {
		if (currentPage < 1 || pageSize < 1) {
			throw new InvalidParamException();
		}
	}

	public void addOrder(Order order) {
		this.orders.add(order);
	}

	public void addOrders(Order... orders) {
		Collections.addAll(this.orders, orders);
	}
}
