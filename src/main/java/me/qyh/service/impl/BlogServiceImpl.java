package me.qyh.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.qyh.bean.Scopes;
import me.qyh.config.BlogConfig;
import me.qyh.config.ConfigServer;
import me.qyh.config.FrequencyLimit;
import me.qyh.dao.BlogCategoryDao;
import me.qyh.dao.BlogDao;
import me.qyh.dao.BlogTagDao;
import me.qyh.dao.CommentDao;
import me.qyh.dao.CommentScopeDao;
import me.qyh.dao.TagDao;
import me.qyh.dao.TemporaryBlogDao;
import me.qyh.dao.UserTagDao;
import me.qyh.entity.CommentScope;
import me.qyh.entity.Space;
import me.qyh.entity.User;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogCategory;
import me.qyh.entity.blog.BlogStatus;
import me.qyh.entity.blog.BlogTag;
import me.qyh.entity.blog.TemporaryBlog;
import me.qyh.entity.tag.Tag;
import me.qyh.entity.tag.UserTag;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.helper.cache.SignCache;
import me.qyh.helper.cache.SignCacheEvict;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.CommentPageParam;
import me.qyh.pageparam.Page;
import me.qyh.security.UserContext;
import me.qyh.server.SpaceServer;
import me.qyh.server.UserServer;
import me.qyh.service.BlogService;
import me.qyh.utils.Strings;
import me.qyh.utils.Validators;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service("blogService")
public class BlogServiceImpl extends BaseServiceImpl implements BlogService {

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
	private CommentDao commentDao;
	@Autowired
	private CommentScopeDao commentScopeDao;
	@Autowired
	private TemporaryBlogDao temporaryBlogDao;
	@Autowired
	private ObjectWriter objectWriter;
	@Autowired
	private ObjectReader objectReader;
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

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void insertBlog(Blog blog) throws LogicException {
		BlogCategory category = loadBlogCategory(blog.getCategory().getId());
		super.doAuthencation(blog.getSpace(), category.getSpace());

		BlogConfig config = configServer.getBlogConfig(UserContext.getUser());
		checkInsertFrequency(config.getLimit(), blog.getSpace());

		setBlogSummray(blog, summaryLength);
		temporaryBlogDao.deleteByBlog(blog);
		blogDao.insert(blog);
		insertBlogTag(blog);

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteBlog(Integer id) throws LogicException {
		Blog blog = loadBlog(id);
		super.doAuthencation(UserContext.getSpace(), blog.getSpace());

		if (!isBlogDeleted(blog)) {
			throw new LogicException("error.blog.undeleted");
		}

		deleteBlogFromDb(blog);
	}

	protected void deleteBlogFromDb(Blog blog) {
		temporaryBlogDao.deleteByBlog(blog);
		blogTagDao.deleteByBlog(blog);

		Integer id = blog.getId();
		blogDao.deleteById(blog.getId());

		CommentScope scope = commentScopeDao.selectByScopeAndScopeId(Blog.class.getSimpleName(), id.toString());

		if (scope != null) {
			commentDao.deleteByCommentScope(scope);
			commentScopeDao.deleteById(scope.getId());
		}
	}

	@Override
	@Transactional(readOnly = true)
	@SignCache(cacheName = "blogCache", cacheKey = "'blog-'+#id", condition = "#result.scope == T(me.qyh.entity.Scope).PUBLIC")
	public Blog getBlog(Integer id) throws LogicException {
		Blog blog = loadBlog(id);
		if (isBlogDeleted(blog)) {
			throw new LogicException("error.blog.notexists");
		}

		super.doAuthencation(spaceServer.getScopes(UserContext.getUser(), blog.getSpace()), blog.getScope());
		User user = userServer.getUserBySpace(blog.getSpace());
		BlogConfig config = configServer.getBlogConfig(user);
		blog.setContent(config.getClean().handle(blog.getContent()));
		blog.setComments(getComments(blog));
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
	public void updateHits(Integer blog, int hits) {
		blogDao.updateHits(blog, hits);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Blog> findBlogs(BlogPageParam param) {
		validBlogPageParam(param);
		List<Blog> datas = blogDao.selectPage(param);
		if (!datas.isEmpty()) {
			for (Blog data : datas) {
				data.setComments(getComments(data));
			}
		}
		int count = blogDao.selectCount(param);
		return new Page<Blog>(param, count, datas);
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

		blog.setStatus(BlogStatus.RECYCLER);
		blogDao.update(blog);
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
		blogDao.update(toUpdate);

		Set<Tag> oldTags = db.getTags();
		if (!oldTags.isEmpty()) {
			blogTagDao.deleteByBlog(db);
		}
		insertBlogTag(toUpdate);

		temporaryBlogDao.deleteByBlog(db);
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
			blog.setWriteDate(now);
			blog.setStatus(BlogStatus.NORMAL);
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

		blog.setStatus(BlogStatus.NORMAL);
		blogDao.update(blog);
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
		TemporaryBlog tBlog = temporaryBlogDao.selectById(id);

		if (tBlog == null) {
			throw new LogicException("error.blog.temporary.notexists");
		}

		super.doAuthencation(UserContext.getSpace(), tBlog.getSpace());

		try {
			return tBlog.toBlog(objectReader);
		} catch (IOException e) {
			throw new SystemException(e);
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void insertOrUpdateTemporaryBlog(Blog blog) throws LogicException {
		TemporaryBlog tBlog;
		try {
			tBlog = blog.getTemporaryBlog(objectWriter);
		} catch (JsonProcessingException e) {
			throw new SystemException(e.getMessage(), e);
		}

		List<TemporaryBlog> tBlogs = temporaryBlogDao.selectByBlog(blog);
		if (tBlogs.isEmpty()) {
			if (blog.hasId()) {
				Blog _blog = blogDao.selectById(blog.getId());
				if (_blog != null) {
					super.doAuthencation(UserContext.getSpace(), _blog.getSpace());
				}
				tBlog.setBlog(_blog);
			} else {
				tBlog.setBlog(null);
			}
			temporaryBlogDao.insert(tBlog);
		} else {
			TemporaryBlog db = tBlogs.get(0);
			super.doAuthencation(UserContext.getSpace(), db.getSpace());
			Date now = new Date();
			long time = now.getTime() - db.getSaveDate().getTime();
			if (time < temporarySaveFrequency) {
				throw new LogicException("error.frequenceOperation", (temporarySaveFrequency - time) / 1000);
			}

			tBlog.setId(db.getId());
			tBlog.setSaveDate(now);
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

	private int getComments(Blog blog) {
		CommentScope scope = commentScopeDao.selectByScopeAndScopeId(Blog.class.getSimpleName(),
				blog.getId().toString());

		if (scope == null) {
			return 0;
		}

		CommentPageParam param = new CommentPageParam();
		param.setScope(scope);
		param.setParent(null);

		return commentDao.selectCount(param);
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
		return (blog == null || blog.isDeleted());
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
}
