package me.qyh.entity.validator;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.entity.Editor;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.Blog.BlogStatus;
import me.qyh.entity.blog.BlogCategory;
import me.qyh.entity.tag.Tag;
import me.qyh.utils.Strings;
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
	@Value("${config.validation.blog.scheduledDay}")
	private int scheduledDay;// 用户定时发布博客，最多能延后多少天

	@Override
	public boolean supports(Class<?> clazz) {
		return Blog.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		Blog blog = (Blog) o;
		BlogStatus status = blog.getStatus();
		if (status == null) {
			e.rejectValue("status", "validation.blog.status.blank");
			return;
		}
		Date writeDate = blog.getWriteDate();
		switch (status) {
		case NORMAL:
			if (writeDate != null) {
				e.rejectValue("writeDate", "validation.blog.writeDate.invalid");
				return;
			}
			break;
		case SCHEDULED:
			if (writeDate == null) {
				e.rejectValue("writeDate", "validation.blog.writeDate.blank");
				return;
			}
			Date now = new Date();
			if (now.after(writeDate)) {
				e.rejectValue("writeDate", "validation.blog.writeDate.invalid");
				return;
			}
			long time = writeDate.getTime() - now.getTime();
			if (time > scheduledDay * 24 * 60 * 60 * 1000) {
				e.rejectValue("writeDate", "validation.blog.writeDate.invalid");
				return;
			}
			break;
		}
		Editor editor = blog.getEditor();
		if (editor == null) {
			e.rejectValue("title", "validation.blog.editor.blank");
			return;
		}
		if (Editor.MD.equals(editor)) {
			String display = blog.getDisplay();
			if (Validators.isEmptyOrNull(display, true) || !Validators.hasText(display)) {
				e.rejectValue("content", "validation.blog.content.blank");
				return;
			}
			if (display.length() > contentMaxLength) {
				e.rejectValue("content", "validation.blog.content.toolong", new Object[] { contentMaxLength },
						"博客内容不能超过" + contentMaxLength + "个字符");
				return;
			}
		}
		String title = blog.getTitle();
		if (Validators.isEmptyOrNull(title, true)) {
			e.rejectValue("title", "validation.blog.title.blank");
			return;
		}
		String content = blog.getContent();
		if (Validators.isEmptyOrNull(content, true) || !Validators.hasText(content)) {
			e.rejectValue("content", "validation.blog.content.blank");
			return;
		}
		BlogCategory bc = blog.getCategory();
		if (bc == null || !bc.hasId()) {
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
			e.rejectValue("title", "validation.blog.title.toolong", new Object[] { titleMaxLength },
					"博客标题不能超过" + titleMaxLength + "个字符");
			return;
		}
		if (content.length() > contentMaxLength) {
			e.rejectValue("content", "validation.blog.content.toolong", new Object[] { contentMaxLength },
					"博客内容不能超过" + contentMaxLength + "个字符");
			return;
		}
		Set<Tag> tags = blog.getTags();
		if (!tags.isEmpty()) {
			if (tags.size() > tagsMaxSize) {
				e.rejectValue("tags", "validation.blog.tags.oversize", new Object[] { tagsMaxSize },
						"博客标签数不能超过" + tagsMaxSize + "个");
				return;
			}
			for (Tag tag : tags) {
				String name = tag.getName();
				if (Validators.isEmptyOrNull(name, true)) {
					e.rejectValue("tags", "validation.blog.tags.name.blank");
					return;
				}
				name = Strings.replaceOtherSymbols(name);
				if (name.trim().isEmpty()) {
					e.rejectValue("tags", "validation.blog.tags.name.blank");
					return;
				}
				if (name.length() > tagNameMaxLength) {
					e.rejectValue("tags", "validation.blog.tags.name.toolong", new Object[] { tagNameMaxLength },
							"博客单个标签长度不能超过" + tagNameMaxLength + "个字符");
					return;
				}
				tag.setName(name);
			}
		}
		Integer level = blog.getLevel();
		if (level == null) {
			e.rejectValue("level", "validation.blog.level.empty");
			return;
		}
		if (level < minLevel || level > maxLevel) {
			e.rejectValue("level", "validation.blog.level.invalid", new Object[] { minLevel, maxLevel },
					"博客排序值在" + minLevel + "和" + maxLevel + "之间");
			return;
		}
	}
}
