package me.qyh.helper.htmlclean;

/**
 * 判断某个属性是否允许
 * 
 * @author mhlx
 *
 */
public interface AttributeValueValidator {

	/**
	 * 是否允许
	 * 
	 * @param value
	 *            属性值
	 * @return
	 */
	boolean allow(String value);

}
