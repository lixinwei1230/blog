package me.qyh.helper.htmlclean;

/**
 * 
 * @author mhlx
 *
 */
public class IntegerValidator implements AttributeValueValidator {

	private Integer min;
	private Integer max;

	@Override
	public boolean allow(String value) {
		Integer v = null;
		try {
			v = Integer.parseInt(value);
		} catch (Exception e) {
			return false;
		}
		if (min != null && v < min) {
			return false;
		}
		if (max != null && v > max) {
			return false;
		}
		return true;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

}
