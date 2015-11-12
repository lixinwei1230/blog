package me.qyh.page.widget.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WidgetConfigAttribute {

	/**
	 * 用来区分是何种WidgetConfig的标记，如果无法找到该标记对应的值，将会抛出{@code InvalidParamException}
	 * 
	 * @return
	 */
	abstract String sign() default "widgetSign";

	/**
	 * 是否验证实体，如果为true，将会返回{@code BindingResultWidgetConfig}
	 * 
	 * @return
	 */
	abstract boolean validate() default false;

	/**
	 * 实体名
	 * 
	 * @return
	 */
	abstract String name() default "config";

	/**
	 * 是否通过jackson封装对象 只有ajax请求时才会生效
	 */
	abstract boolean requestBody() default false;

}
