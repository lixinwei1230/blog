package me.qyh.entity.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.entity.blog.BlogCategory;
import me.qyh.utils.Validators;

@Component("blogCategoryValidator")
public class BlogCategoryValidator implements Validator {

	@Value("${config.validation.blog.category.nameMaxLength}")
	private int nameMaxLength;
	@Value("${config.validation.blog.category.orderMax}")
	private int orderMax;
	@Value("${config.validation.blog.category.orderMin}")
	private int orderMin;

	@Override
	public boolean supports(Class<?> clazz) {
		return BlogCategory.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		BlogCategory category = (BlogCategory) o;
		String name = category.getName();
		if (Validators.isEmptyOrNull(name, true)) {
			e.rejectValue("name", "validation.blog.category.name.blank");
			return;
		}
		if (name.length() > nameMaxLength) {
			e.rejectValue("name", "validation.blog.category.name.toolong",
					new Object[] { nameMaxLength },
					"博客分类不能超过" + nameMaxLength + "个字符");
			return;
		}
		int order = category.getOrder();
		if (order < orderMin || order > orderMax) {
			e.rejectValue("order", "validation.blog.category.order.invalid",
					new Object[] { orderMin, orderMax },
					"博客分类顺序在" + orderMin + "~" + orderMax + "之间");
			return;
		}
	}

}
