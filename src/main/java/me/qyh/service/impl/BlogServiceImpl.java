package me.qyh.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import me.qyh.bean.Scopes;
import me.qyh.config.BlogCommentConfig;
import me.qyh.config.BlogConfig;
import me.qyh.config.ConfigServer;
import me.qyh.config.FrequencyLimit;
import me.qyh.dao.BlogCategoryDao;
import me.qyh.dao.BlogCommentDao;
import me.qyh.dao.BlogDao;
import me.qyh.dao.BlogTagDao;
import me.qyh.dao.TagDao;
import me.qyh.dao.TemporaryBlogDao;
import me.qyh.dao.UserTagDao;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.Scope;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogCategory;
import me.qyh.entity.blog.BlogComment;
import me.qyh.entity.blog.BlogTag;
import me.qyh.entity.blog.TemporaryBlog;
import me.qyh.entity.tag.Tag;
import me.qyh.entity.tag.UserTag;
import me.qyh.exception.BusinessAccessDeinedException;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.helper.cache.SignCache;
import me.qyh.helper.cache.SignCacheEvict;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.helper.htmlclean.HtmlContentHandler;
import me.qyh.helper.lucene.BlogIndexHandler;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.CommentPageParam;
import me.qyh.pageparam.Page;
import me.qyh.security.UserContext;
import me.qyh.server.SpaceServer;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.server.UserServer;
import me.qyh.service.BlogService;
import me.qyh.utils.Strings;
import me.qyh.utils.Validators;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("blogService")
public class BlogServiceImpl extends BaseServiceImpl implements BlogService{

	private static final String COMMENT_TIP_TEMPLATE = "tip/comment_blog.ftl";
	
	@Autowired
	private BlogCategoryDao blogCategoryDao;
	@Autowired
	protected BlogDao blogDao;
	@Autowired
	private BlogTagDao blogTagDao;
	@Autowired
	private TagDao tagDao;
	@Autowired
	private UserTagDao userTagDao;
	@Autowired
	private BlogCommentDao commentDao;
	@Autowired
	private TemporaryBlogDao temporaryBlogDao;
	@Value("${config.blog.summary.length}")
	private int summaryLength;
	@Value("${config.blog.category.maxSize}")
	private int categoryMaxSize;
	@Value("${config.blog.tags.searchMaxSize}")
	private int tagsMaxSizeWhenSearch;
	@Autowired
	private SpaceServer spaceServer;
	@Autowired
	private UserServer userServer;
	@Autowired
	private ConfigServer configServer;
	@Value("${config.blog.temporarySaveFrequency}")
	private long temporarySaveFrequency;
	@Autowired
	private HtmlContentHandler commentHtmlHandler;
	@Autowired
	private TipServer tipServer;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private WebFreemarkers freeMarkers;
	@Autowired
	protected BlogIndexHandler blogIndexHandler;

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void insertBlog(Blog blog) throws LogicException {
		BlogCategory category = loadBlogCategory(blog.getCategory().getId());
		super.doAuthencation(blog.getSpace(), category.getSpace());

		BlogConfig config = configServer.getBlogConfig(UserContext.getUser());
		checkInsertFrequency(config.getLimit(), blog.getSpace());

		setBlogSummray(blog, summaryLength);
		temporaryBlogDao.deleteByBlog(blog);
		
		if(!blog.isScheduled()){
			blog.setWriteDate(new Date());
		}
		
		blog.setDel(false);
		blogDao.insert(blog);
		insertBlogTag(blog);
		
		blog.setCategory(category);
		
		blogIndexHandler.rebuildBlogIndex(blog);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteBlog(Integer id ) throws LogicException {
		Blog blog = loadBlog(id);
		super.doAuthencation(UserContext.getSpace(), blog.getSpace());

		if(!blog.isScheduled() && !isBlogDeleted(blog)){
			throw new LogicException("error.blog.undeleted");
		}

		deleteBlogFromDb(blog);
	}

	protected void deleteBlogFromDb(Blog blog) {
		temporaryBlogDao.deleteByBlog(blog);
		blogTagDao.deleteByBlog(blog);
		commentDao.deleteByBlog(blog);

		blogDao.deleteById(blog.getId());
		
		blogIndexHandler.removeBlogIndex(blog);
	}

	@Override
	@Transactional(readOnly = true)
	@SignCache(cacheName = "blogCache", cacheKey = "'blog-'+#id", condition = "#result.scope == T(me.qyh.entity.Scope).PUBLIC")
	public Blog getBlog(Integer id) throws LogicException {
		Blog blog = loadBlog(id);
		if (isBlogDeleted(blog)) {
			throw new LogicException("error.blog.notexists");
		}
		if(blog.isScheduled() && !blog.getSpace().equals(UserContext.getSpace())){
			throw new LogicException("error.blog.notexists");
		}

		super.doAuthencation(spaceServer.getScopes(UserContext.getUser(), blog.getSpace()), blog.getScope());
		User user = userServer.getUserBySpace(blog.getSpace());
		BlogConfig config = configServer.getBlogConfig(user);
		blog.setContent(config.getClean().handle(blog.getContent()));
		return blog;
	}

	@Override
	@Transactional(readOnly = true)
	public List<BlogCategory> findBlogCategorys(Space space) {
		return blogCategoryDao.selectBySpace(space);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public BlogCategory insertOrUpdateBlogCategory(BlogCategory category) throws LogicException {
		if (category.getId() == null) {
			insertBlogCategory(category);
		} else {
			updateBlogCategory(category);
		}
		return category;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteBlogCategory(Integer id) throws LogicException {
		BlogCategory db = loadBlogCategory(id);
		super.doAuthencation(UserContext.getSpace(), db.getSpace());

		BlogPageParam param = new BlogPageParam();
		param.setCategory(db);
		if (blogDao.selectCount(param) > 0) {
			throw new LogicException("error.blog.category.hasBlog");
		}

		blogCategoryDao.deleteById(id);

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateHits(Integer blog, int hits) throws LogicException{
		Blog db = loadBlog(blog);
		if (isBlogDeleted(db) || db.isScheduled()) {
			throw new LogicException("error.blog.notexists");
		}
		blogDao.updateHits(blog, hits);
		
		db.setHits(db.getHits() + hits);
		blogIndexHandler.rebuildBlogIndex(db);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Blog> findBlogs(BlogPageParam param) {
		validBlogPageParam(param);
		Page<Blog> page = blogIndexHandler.search(param);
		List<Blog> blogs = page.getDatas();
		List<Blog> results = new ArrayList<Blog>();
		if(!blogs.isEmpty()){
			for(Blog blog : blogs){
				Blog preview = blogDao.selectPreview(blog.getId());
				if(preview != null){
					preview.setTags(blog.getTags());
					results.add(preview);
				}
			}
			page.setDatas(results);
		}
		return page;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@SignCacheEvict(cacheName = "blogCache", cacheKey = "'blog-'+#id")
	public void deleteBlogLogic(Integer id) throws LogicException {

		Blog blog = loadBlog(id);
		super.doAuthencation(UserContext.getSpace(), blog.getSpace());

		if (isBlogDeleted(blog)) {
			throw new LogicException("error.blog.deleted");
		}

		blog.setDel(true);
		blogDao.updateDel(blog);
		
		blogIndexHandler.rebuildBlogIndex(blog);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@SignCacheEvict(cacheName = "blogCache", cacheKey = "'blog-'+#toUpdate.id")
	public void updateBlog(Blog toUpdate) throws LogicException {
		Blog db = loadBlog(toUpdate.getId());

		if (isBlogDeleted(db)) {
			throw new LogicException("error.blog.deleted");
		}

		Space current = UserContext.getSpace();
		super.doAuthencation(current, db.getSpace());

		BlogCategory category = loadBlogCategory(toUpdate.getCategory().getId());
		super.doAuthencation(current, category.getSpace());

		setBlogSummray(toUpdate, summaryLength);
		
		if(db.isScheduled() && !toUpdate.isScheduled()){
			toUpdate.setWriteDate(new Date());
		}
		blogDao.update(toUpdate);

		Set<Tag> oldTags = db.getTags();
		if (!oldTags.isEmpty()) {
			blogTagDao.deleteByBlog(db);
		}
		insertBlogTag(toUpdate);

		temporaryBlogDao.deleteByBlog(db);
		
		blogIndexHandler.rebuildBlogIndex(loadBlog(db.getId()));
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void handleTemporaryBlog(Blog blog) throws LogicException {
		List<TemporaryBlog> tBlogs = temporaryBlogDao.selectByBlog(blog);
		if (tBlogs.isEmpty()) {
			throw new LogicException("error.blog.temporary.notexists");
		}

		super.doAuthencation(UserContext.getSpace(), tBlogs.get(0).getSpace());
		boolean insert = true;
		if (blog.hasId()) {
			Blog db = blogDao.selectById(blog.getId());
			insert = (db == null);
		}

		Date now = new Date();
		if (insert) {
			if(!blog.isScheduled()){
				blog.setWriteDate(now);
			}
			insertBlog(blog);
		} else {
			blog.setLastModifyDate(now);
			updateBlog(blog);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void recover(Integer id) throws LogicException {
		Blog blog = loadBlog(id);
		super.doAuthencation(UserContext.getSpace(), blog.getSpace());

		if (!isBlogDeleted(blog)) {
			throw new LogicException("error.blog.undeleted");
		}

		blog.setDel(false);
		blogDao.updateDel(blog);
		
		blogIndexHandler.rebuildBlogIndex(blog);
	}

	@Override
	@Transactional(readOnly = true)
	public TemporaryBlog getTemporaryBlog(Blog blog) {
		List<TemporaryBlog> tBlogs = temporaryBlogDao.selectByBlog(blog);

		return tBlogs.isEmpty() ? null : tBlogs.get(0);
	}

	@Override
	@Transactional(readOnly = true)
	public Blog getTemporaryBlog(Integer id) throws LogicException {
		Blog tBlog = temporaryBlogDao.selectById(id);

		if (tBlog == null) {
			throw new LogicException("error.blog.temporary.notexists");
		}

		super.doAuthencation(UserContext.getSpace(), tBlog.getSpace());

		return tBlog;

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void insertOrUpdateTemporaryBlog(Blog blog) throws LogicException {

		List<TemporaryBlog> tBlogs = temporaryBlogDao.selectByBlog(blog);
		if (tBlogs.isEmpty()) {
			if (blog.hasId()) {
				Blog _blog = blogDao.selectById(blog.getId());
				if (_blog != null) {
					super.doAuthencation(UserContext.getSpace(), _blog.getSpace());
				}
			}
			temporaryBlogDao.insert(new TemporaryBlog(blog));
		} else {
			TemporaryBlog db = tBlogs.get(0);
			super.doAuthencation(UserContext.getSpace(), db.getSpace());
			Date now = new Date();
			long time = now.getTime() - db.getSaveDate().getTime();
			if (time < temporarySaveFrequency) {
				throw new LogicException("error.frequenceOperation", (temporarySaveFrequency - time) / 1000);
			}
			TemporaryBlog tBlog = new TemporaryBlog(blog);
			tBlog.setDummyId(db.getDummyId());
			temporaryBlogDao.update(tBlog);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Blog> findAroundBlogs(Integer id, BlogPageParam param) {
		validBlogPageParam(param);
		List<Blog> blogs = new ArrayList<Blog>();
		Blog previous = blogDao.getPreviousBlog(id, param);
		Blog next = blogDao.getNextBlog(id, param);
		if (previous != null) {
			blogs.add(previous);
		}
		if (next != null) {
			blogs.add(next);
		}
		return blogs;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<BlogComment> findComments(CommentPageParam param) throws LogicException {
		Blog query = param.getBlog();
		if(query != null){
			Blog blog = loadBlog(param.getBlog().getId());
			if (blog.getDel()) {
				throw new LogicException("error.blog.notexists");
			}
			if (blog.getIsPrivate() && !blog.getSpace().equals(UserContext.getSpace())) {
				throw new BusinessAccessDeinedException();
			}
		}
		Page<BlogComment> page = _findComments(param);
		if (param.getParent() == null) {
			List<BlogComment> datas = page.getDatas();
			if (!datas.isEmpty()) {
				for (BlogComment comment : datas) {
					param.setCurrentPage(1);
					param.setParent(comment);
					comment.setPage(_findComments(param));
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public BlogComment insertComment(BlogComment comment) throws LogicException {
		Blog blog = loadBlog(comment.getBlog().getId());
		if (isBlogDeleted(blog)) {
			throw new LogicException("error.blog.deleted");
		}
		if (blog.getIsPrivate() && !blog.getSpace().equals(UserContext.getSpace())) {
			throw new BusinessAccessDeinedException();
		}
		if (Scope.PRIVATE.equals(blog.getCommentScope()) && !blog.getSpace().equals(UserContext.getSpace())) {
			throw new LogicException("error.blog.notAllowComment");
		}
		
		BlogCommentConfig config = configServer.getBlogCommentConfig(blog, comment.getUser());
		checkCommentFrequencyLimit(config.getLimit(), comment.getUser());

		BlogComment parent = comment.getParent();
		if (parent != null) {
			parent = commentDao.selectById(parent.getId());
			if (parent == null) {
				throw new LogicException("error.comment.parentNotFound");
			}
			if (parent.getParent() != null) {
				throw new SystemException("当前评论不能作为父评论，因为当前评论的父评论还有父评论");
			}
			BlogComment reply = commentDao.selectById(comment.getReply().getId());
			if (reply == null) {
				throw new LogicException("error.comment.replyNotFound");
			}
			comment.setReply(reply);
			comment.setReplyTo(reply.getIsAnonymous() ? null : reply.getUser());
		}
		
		if(parent == null){
			blogDao.updateComments(blog.getId(), 1);
			
			blog.setComments(blog.getComments()+1);
			blogIndexHandler.rebuildBlogIndex(blog);
		}

		commentDao.insert(comment);

		sendCommentTip(comment, blog);

		BlogComment inserted = cleanComment(commentDao.selectById(comment.getId()));
		return inserted;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteComment(Integer id) throws LogicException {
		BlogComment comment = commentDao.selectById(id);
		if (comment == null) {
			throw new LogicException("error.comment.notFound");
		}

		User current = UserContext.getUser();
		User owner = comment.getUser();
		boolean hasParent = (comment.getParent() != null);
		Blog blog = loadBlog(comment.getBlog().getId());
		if (isBlogDeleted(blog)) {
			throw new LogicException("error.blog.deleted");
		}
		Space space = blog.getSpace();
		// 如果当前用户不是评论域拥有人、回复所有人或者评论所有人,或者超级管理员
		if (!space.equals(UserContext.getSpace()) && !current.equals(owner)
				&& !current.hasRole(RoleEnum.ROLE_SUPERVISOR)) {
			boolean hasPermission = false;
			if (hasParent) {
				BlogComment parent = commentDao.selectById(comment.getParent().getId());
				User parentOwner = parent.getUser();
				hasPermission = current.equals(parentOwner);
			}
			if (!hasPermission) {
				throw new BusinessAccessDeinedException();
			}
		}
		if (!hasParent) {
			blogDao.updateComments(blog.getId(), -1);
			commentDao.deleteByParent(comment);
			
			blog.setComments(blog.getComments() - 1);
			blogIndexHandler.rebuildBlogIndex(blog);
		}
		commentDao.deleteById(id);
	}

	private void insertBlogTag(Blog blog) {
		Set<Tag> tags = blog.getTags();
		if (!Validators.isEmptyOrNull(tags)) {
			for (Tag tag : tags) {
				String handledTagName = this.handleTagName(tag);
				Tag db = tagDao.selectByName(handledTagName);
				if (db == null) {
					tag.setCreateDate(new Date());
					tagDao.insert(tag);
				} else {
					tag.setId(db.getId());
				}

				User user = UserContext.getUser();
				UserTag ut = userTagDao.selectByTag(tag, user);
				if (ut == null) {
					UserTag _ut = new UserTag();
					_ut.setUser(user);
					_ut.setTag(tag);
					userTagDao.insert(_ut);
				}
			}

			List<BlogTag> blogTags = blog.getBlogTags();
			for (BlogTag blogTag : blogTags) {
				blogTagDao.insert(blogTag);
			}
		}
	}

	protected String handleTagName(Tag tag) {
		return Strings.deleteWhitespace(tag.getName()).toUpperCase();
	}

	private void insertBlogCategory(BlogCategory category) throws LogicException {
		BlogCategory db = blogCategoryDao.selectBySpaceAndName(category.getSpace(), category.getName());

		if (db != null) {
			throw new LogicException("error.blog.category.duplicate", db.getName());
		}

		int count = blogCategoryDao.selectCountBySpace(category.getSpace());
		if (count >= categoryMaxSize) {
			throw new LogicException("error.blog.category.oversize", categoryMaxSize);
		}

		blogCategoryDao.insert(category);
	}

	private void updateBlogCategory(BlogCategory toUpdate) throws LogicException {
		BlogCategory db = loadBlogCategory(toUpdate.getId());
		super.doAuthencation(UserContext.getSpace(), db.getSpace());

		BlogCategory toCheck = blogCategoryDao.selectBySpaceAndName(db.getSpace(), toUpdate.getName());

		if (toCheck != null && !toCheck.equals(toUpdate)) {
			throw new LogicException("error.blog.category.duplicate", toCheck.getName());
		}

		blogCategoryDao.update(toUpdate);
	}

	private BlogCategory loadBlogCategory(Integer id) throws LogicException {

		BlogCategory category = blogCategoryDao.selectById(id);

		if (category == null) {
			throw new LogicException("error.blog.category.notexists");
		}

		return category;
	}

	protected Blog loadBlog(Integer id) throws LogicException {
		Blog blog = blogDao.selectById(id);
		if (blog == null) {
			throw new LogicException("error.blog.notexists");
		}

		return blog;
	}

	private boolean isBlogDeleted(Blog blog) {
		return (blog == null || blog.getDel());
	}

	/**
	 * 提取博客摘要
	 * 
	 * @param length
	 *            博客摘要最大长度
	 */
	public void setBlogSummray(Blog blog, int length) {
		Document doc = Jsoup.parseBodyFragment(blog.getContent());
		String summary = doc.text();
		if (summary.length() > length) {
			summary = summary.substring(0, length);
		}
		blog.setSummary(summary);
	}

	private void checkInsertFrequency(FrequencyLimit limit, Space space) throws LogicException {
		if (limit != null) {
			BlogPageParam param = new BlogPageParam();
			param.setBegin(limit.getBegin());
			param.setEnd(limit.getEnd());
			param.setSpace(space);

			int count = blogDao.selectCount(param);
			if (count >= limit.getLimit()) {
				throw new LogicException("error.frequency.limit.blog");
			}
		}
	}

	private void validBlogPageParam(BlogPageParam param) {
		Scopes max = spaceServer.getScopes(UserContext.getUser(), param.getSpace());
		if (!max.contains(param.getScopes())) {
			param.setScopes(max);
		}
		Set<String> tags = param.getTags();
		if (!Validators.isEmptyOrNull(tags) && tags.size() > tagsMaxSizeWhenSearch) {
			param.setTags(new HashSet<String>(new ArrayList<String>(tags).subList(0, tagsMaxSizeWhenSearch)));
		}
	}

	private Page<BlogComment> _findComments(CommentPageParam param) {
		int total = commentDao.selectCount(param);
		List<BlogComment> datas = commentDao.selectPage(param);
		if (!datas.isEmpty()) {
			for (BlogComment comment : datas) {
				cleanComment(comment);
			}
		}
		return new Page<BlogComment>(param, total, datas);
	}

	private BlogComment cleanComment(BlogComment comment) {
		String content = comment.getContent();
		comment.setContent(commentHtmlHandler.handle(content));
		return comment;
	}

	protected void checkCommentFrequencyLimit(FrequencyLimit limit, User user) throws LogicException {
		if (limit != null) {
			int count = commentDao.selectCountByDate(limit.getBegin(), limit.getEnd(), user);

			if (count >= limit.getLimit()) {
				throw new LogicException("error.frequency.limit.comment");
			}
		}
	}

	private void sendCommentTip(BlogComment comment, Blog blog) throws LogicException {
		String title = "";
		User receiver = null;
		BlogComment reply = comment.getReply();
		User commenter = comment.getUser();
		User scopeUser = userServer.getUserBySpace(blog.getSpace());
		boolean sendTip;
		if (reply != null) {
			sendTip = !reply.getUser().equals(commenter);
		} else {
			sendTip = !scopeUser.equals(commenter);
		}
		if (!sendTip) {
			return;
		}
		Locale locale = LocaleContextHolder.getLocale();
		if (reply != null) {
			if (comment.getIsAnonymous()) {
				title = messageSource.getMessage("blog.comment.anonymous.reply", new Object[] {}, locale);
			} else {
				title = messageSource.getMessage("blog.comment.user.reply", new Object[] { commenter.getNickname() },
						locale);
			}
			receiver = reply.getUser();
		} else {
			if (comment.getIsAnonymous()) {
				title = messageSource.getMessage("blog.comment.anonymous.comment", new Object[] {}, locale);
			} else {
				title = messageSource.getMessage("blog.comment.user.comment", new Object[] { commenter.getNickname() },
						locale);
			}
			receiver = scopeUser;
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", title);
		model.put("comment", comment);
		model.put("space", blog.getSpace());
		String content = freeMarkers.processTemplateIntoString(COMMENT_TIP_TEMPLATE, model);

		TipMessage message = new TipMessage();
		message.setTitle(title);
		message.setContent(content);
		message.setReceiver(receiver);
		message.setSender(userServer.getMessagers().get(0));

		tipServer.sendTip(message);
	}
	
}
