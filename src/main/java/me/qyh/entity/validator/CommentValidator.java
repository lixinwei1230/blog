package me.qyh.entity.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import me.qyh.entity.Comment;
import me.qyh.entity.CommentScope;
import me.qyh.utils.Validators;

@Component("commentValidator")
public class CommentValidator implements Validator {

	@Value("${config.validation.comment.scope.scopeMaxLength}")
	private int scopeMaxLength;
	@Value("${config.validation.comment.scope.scopeIdMaxLength}")
	private int scopeIdMaxLength;
	@Value("${config.validation.comment.titleMaxLength}")
	private int titleMaxLength;
	@Value("${config.validation.comment.contentMaxLength}")
	private int contentMaxLength;

	@Override
	public boolean supports(Class<?> clazz) {
		return Comment.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors e) {
		Comment comment = (Comment) o;
		CommentScope target = comment.getScope();
		if (target == null) {
			e.rejectValue("scope", "validation.comment.scope.empty");
			return;
		}
		String scope = target.getScope();
		if (Validators.isEmptyOrNull(scope, true)) {
			e.rejectValue("scope", "validation.comment.scope.scope.empty");
			return;
		}
		if (scope.length() > scopeMaxLength) {
			e.rejectValue("scope", "validation.comment.scope.scope.toolong", new Object[] { scopeMaxLength },
					"目标空间不能超过" + scopeMaxLength + "个字符");
			return;
		}
		String tid = target.getScopeId();
		if (Validators.isEmptyOrNull(tid, true)) {
			e.rejectValue("scope", "validation.comment.scope.scopeId.empty");
			return;
		}
		if (tid.length() > scopeIdMaxLength) {
			e.rejectValue("scope", "validation.comment.scope.scopeId.toolong", new Object[] { scopeIdMaxLength },
					"目标空间ID不能超过" + scopeIdMaxLength + "个字符");
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
