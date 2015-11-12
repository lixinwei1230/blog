package me.qyh.entity.validator;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.entity.blog.Blog;
import me.qyh.entity.tag.WebTag;
import me.qyh.utils.Validators;

@Component("blogValidator")
public class BlogValidator implements Validator {

	@Value("${config.validation.blog.titleMaxLength}")
	private int titleMaxLength;
	@Value("${config.validation.blog.contentMaxLength}")
	private int contentMaxLength;
	@Value("${config.validation.blog.tagsMaxSize}")
	private int tagsMaxSize;
	@Value("${config.validation.blog.tagNameMaxLength}")
	private int tagNameMaxLength;
	@Value("${config.validation.blog.minLevel}")
	private int minLevel;
	@Value("${config.validation.blog.maxLevel}")
	private int maxLevel;

	@Override
	public boolean supports(Class<?> clazz) {
		return Blog.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		Blog blog = (Blog) o;
		String title = blog.getTitle();
		if (Validators.isEmptyOrNull(title, true)) {
			e.rejectValue("title", "validation.blog.title.blank");
			return;
		}
		String content = blog.getContent();
		if (Validators.isEmptyOrNull(content, true)
				|| !Validators.hasText(content)) {
			e.rejectValue("content", "validation.blog.content.blank");
			return;
		}
		if (blog.getCategory() == null) {
			e.rejectValue("category", "validation.blog.category.blank");
			return;
		}
		if (blog.getFrom() == null) {
			e.rejectValue("from", "validation.blog.from.blank");
			return;
		}
		if (blog.getScope() == null) {
			e.rejectValue("scope", "validation.blog.scope.blank");
			return;
		}
		if (blog.getCommentScope() == null) {
			e.rejectValue("commentScope", "validation.blog.commentScope.blank");
			return;
		}
		if (title.length() > titleMaxLength) {
			e.rejectValue("title", "validation.blog.title.toolong",
					new Object[] { titleMaxLength },
					"博客标题不能超过" + titleMaxLength + "个字符");
			return;
		}
		if (content.length() > contentMaxLength) {
			e.rejectValue("content", "validation.blog.content.toolong",
					new Object[] { contentMaxLength },
					"博客内容不能超过" + contentMaxLength + "个字符");
			return;
		}
		Set<WebTag> tags = new HashSet<WebTag>();
		if (!tags.isEmpty()) {
			if (tags.size() > tagsMaxSize) {
				e.rejectValue("tags", "validation.blog.tags.oversize",
						new Object[] { tagsMaxSize },
						"博客标签数不能超过" + tagsMaxSize + "个");
				return;
			}
			for (WebTag tag : tags) {
				String name = tag.getName();
				if (Validators.isEmptyOrNull(name, true)) {
					e.rejectValue("tags", "validation.blog.tags.name.blank");
					return;
				}
				if (name.length() > tagNameMaxLength) {
					e.rejectValue("tags", "validation.blog.tags.name.toolong",
							new Object[] { tagNameMaxLength },
							"博客单个标签长度不能超过" + tagNameMaxLength + "个字符");
					return;
				}
			}
		}
		Integer level = blog.getLevel();
		if (level == null) {
			e.rejectValue("level", "validation.blog.level.empty");
			return;
		}
		if (level < minLevel || level > maxLevel) {
			e.rejectValue("level", "validation.blog.level.invalid",
					new Object[] { minLevel, maxLevel },
					"博客排序值在" + minLevel + "和" + maxLevel + "之间");
			return;
		}
	}

	public static void main(String[] args) {

	}

}
