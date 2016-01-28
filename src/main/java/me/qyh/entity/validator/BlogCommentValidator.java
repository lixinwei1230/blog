package me.qyh.entity.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogComment;
import me.qyh.utils.Validators;

@Component("commentValidator")
public class BlogCommentValidator implements Validator {

	@Value("${config.validation.comment.titleMaxLength}")
	private int titleMaxLength;
	@Value("${config.validation.comment.contentMaxLength}")
	private int contentMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		return BlogComment.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		BlogComment comment = (BlogComment) o;
		Blog blog = comment.getBlog();
		if (blog == null || !blog.hasId()) {
			e.rejectValue("scope", "validation.comment.blog.empty");
			return;
		}
		String title = comment.getTitle();
		if (Validators.isEmptyOrNull(title, true)) {
			e.rejectValue("title", "validation.comment.title.empty");
			return;
		}
		if (title.length() > titleMaxLength) {
			e.rejectValue("title", "validation.comment.title.toolong", new Object[] { titleMaxLength },
					"评论标题不能超过" + titleMaxLength + "个字符");
			return;
		}
		String content = comment.getContent();
		if (Validators.isEmptyOrNull(content, true) || !Validators.hasText(content)) {
			e.rejectValue("content", "validation.comment.content.empty");
			return;
		}
		if (content.length() > contentMaxLength) {
			e.rejectValue("content", "validation.comment.content.toolong", new Object[] { contentMaxLength },
					"评论内容不能超过" + contentMaxLength + "个字符");
			return;
		}
		if (comment.getParent() != null && comment.getReply() == null) {
			e.rejectValue("reply", "validation.comment.reply.empty");
			return;
		}

		if (comment.getParent() == null && comment.getReply() != null) {
			e.rejectValue("reply", "validation.comment.parent.empty");
			return;
		}
	}
}
