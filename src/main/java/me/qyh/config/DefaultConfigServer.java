package me.qyh.config;

import java.io.File;
import java.util.Date;

import me.qyh.config.FileUploadConfig.SizeLimit;
import me.qyh.config.FileUploadConfig._Config;
import me.qyh.config.FileUploadConfig._ImageConfig;
import me.qyh.entity.CommentScope;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.User;
import me.qyh.helper.htmlclean.HtmlContentHandler;
import me.qyh.page.PageType;
import me.qyh.utils.Files;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 默认配置源
 * 
 * @author mhlx
 * 
 */
@Component
public class DefaultConfigServer implements ConfigServer {

	@Value("${config.upload.attachment.maxSize}")
	private long attachment_maxSizeOfOneFile;
	@Value("${config.upload.attachment.allowFileTypes}")
	private String[] attachment_allowFileTypes;
	@Value("${config.upload.attachment.maxSizeOfQueue}")
	private int attachment_maxSizeOfQueue;
	@Value("${config.upload.image.maxSize}")
	private long image_maxSizeOfOneFile;
	@Value("${config.upload.maxSize}")
	private long maxSizeOfUser;
	@Value("${config.upload.image.allowFileTypes}")
	private String[] image_allowFileTypes;
	@Value("${config.upload.image.maxSizeOfQueue}")
	private int image_maxSizeOfQueue;
	@Value("${config.comment.allowAnonymous}")
	private boolean allowAnonymous;
	@Value("${config.comment.commentOnAuthorOnly}")
	private boolean commentOnAuthorOnly;
	@Value("${config.comment.allowHtml}")
	private boolean allowHtml;
	@Value("${config.page.maxWidgetsAtHomePage}")
	private int maxWidgetsAtHomePage;
	@Value("${config.page.maxWidgetsAtOthers}")
	private int maxWidgetsAtOthers;
	@Value("${config.userWidget.limit}")
	private int userWidgetLimit;
	@Value("${config.upload.avatar.maxSize}")
	private long avatar_maxSize;
	@Value("${config.upload.avatar.allowFileTypes}")
	private String[] avatar_allowFileTypes;
	@Value("${config.upload.avatar.maxWidth}")
	private int avatar_imageMaxWidth;
	@Value("${config.upload.avatar.maxHeight}")
	private int avatar_imageMaxHeight;
	@Value("${config.comment.frequency.limit.minute}")
	private int comment_minute;
	@Value("${config.comment.frequency.limit.count}")
	private int comment_count;
	@Value("${config.blog.frequency.limit.minute}")
	private int blog_minute;
	@Value("${config.blog.frequency.limit.count}")
	private int blog_count;
	@Value("${config.upload.image.maxWidth}")
	private int imageMaxWidth;
	@Value("${config.upload.image.maxHeight}")
	private int imageMaxHeight;
	@Autowired
	private RequestMatcher fileWriteMatcher;
	@Value("${config.image.allowSizes}")
	private int[] fileImage_allowSizes;
	@Value("${config.avatar.allowSizes}")
	private int[] avatar_allowSizes;
	@Autowired
	private HtmlContentHandler fullBlogHtmlClean;
	@Autowired
	private HtmlContentHandler blogHtmlClean;
	@Autowired
	private HtmlContentHandler fullWidgetHtmlClean;
	@Autowired
	private HtmlContentHandler widgetHtmlClean;

	@Override
	public CommentConfig getCommentConfig(CommentScope target) {
		CommentConfig config = new CommentConfig();
		config.setCommentOnAuthorOnly(commentOnAuthorOnly);
		config.setAllowAnonymous(allowAnonymous);
		config.setAllowHtml(allowHtml);

		Date now = new Date();
		Date small = DateUtils.addMinutes(now, -comment_minute);

		config.setLimit(new FrequencyLimit(small, now, comment_count));
		return config;
	}

	@Override
	public PageConfig getPageConfig(User user) {
		PageConfig config = new PageConfig();
		for (PageType type : PageType.values()) {
			config.addWidgetCountLimits(type, maxWidgetsAtOthers);
		}
		config.addWidgetCountLimits(PageType.HOMEPAGE, maxWidgetsAtHomePage);
		config.setUserWidgetLimit(userWidgetLimit);
		
		if (user.hasRole(RoleEnum.ROLE_SUPERVISOR)) {
			config.setClean(fullWidgetHtmlClean);
		} else {
			config.setClean(widgetHtmlClean);
		}
		return config;
	}

	@Override
	public _ImageConfig getAvatarConfig(User user) {
		return new _ImageConfig(new DefaultSizeLimit(avatar_maxSize),
				avatar_allowFileTypes, 1, avatar_imageMaxWidth,
				avatar_imageMaxHeight);
	}

	@Override
	public BlogConfig getBlogConfig(User user) {
		BlogConfig config = new BlogConfig();

		Date now = new Date();
		Date small = DateUtils.addMinutes(now, -blog_minute);
		config.setLimit(new FrequencyLimit(small, now, blog_count));

		if (user.hasRole(RoleEnum.ROLE_SUPERVISOR)) {
			config.setClean(fullBlogHtmlClean);
		} else {
			config.setClean(blogHtmlClean);
		}

		return config;
	}

	@Override
	public FileUploadConfig getFileUploadConfig(User user) {
		_Config config = new _Config(new DefaultSizeLimit(
				attachment_maxSizeOfOneFile), attachment_allowFileTypes,
				attachment_maxSizeOfQueue);
		_ImageConfig imConfig = new _ImageConfig(new DefaultSizeLimit(
				image_maxSizeOfOneFile), image_allowFileTypes,
				image_maxSizeOfQueue, imageMaxWidth, imageMaxHeight);
		return new FileUploadConfig(maxSizeOfUser, config, imConfig);
	}

	@Override
	public FileWriteConfig getFileWriteConfig() {
		FileWriteConfig config = new FileWriteConfig(fileWriteMatcher,
				new DefaultImageZoomMatcher(fileImage_allowSizes));
		return config;
	}

	@Override
	public FileWriteConfig getAvatarWriteConfig() {
		FileWriteConfig config = new FileWriteConfig(fileWriteMatcher,
				new DefaultImageZoomMatcher(avatar_allowSizes));
		return config;
	}

	private class DefaultImageZoomMatcher implements ImageZoomMatcher {

		private int[] allowSizes;

		@Override
		public boolean zoom(Integer size, File file) {
			if ("gif".equalsIgnoreCase(Files.getFileExtension(file))) {
				return false;
			}
			if (size != null && size > 0) {
				for (int allowSize : allowSizes) {
					if (size == allowSize) {
						return true;
					}
				}
			}
			return false;
		}

		private DefaultImageZoomMatcher(int[] allowSizes) {
			this.allowSizes = allowSizes;
		}
	}

	private class DefaultSizeLimit implements SizeLimit {

		private long maxSizeOfOneFile;

		@Override
		public Result allow(MultipartFile file) {
			if (maxSizeOfOneFile > file.getSize()) {
				return new Result(true, maxSizeOfOneFile);
			} else {
				return new Result(false, maxSizeOfOneFile);
			}
		}

		public DefaultSizeLimit(long maxSizeOfOneFile) {
			this.maxSizeOfOneFile = maxSizeOfOneFile;
		}

	}
}
