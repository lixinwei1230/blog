package me.qyh.entity.blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.qyh.entity.Editor;
import me.qyh.entity.Id;
import me.qyh.entity.Scope;
import me.qyh.entity.Space;
import me.qyh.entity.tag.Tag;
import me.qyh.helper.html.JsonHtmlXssSerializer;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 博客
 * 
 * @author mhlx
 *
 */
@JsonIgnoreProperties(value = { "temporaryBlog" })
public class Blog extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String title;// 博客标题
	private String content;// 博客内容(用户原始内容)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date writeDate;// 撰写日期
	private BlogCategory category;// 博客分类
	private Scope scope;// 博客可见度
	private Set<Tag> tags = new HashSet<Tag>();// 博客标签
	private int hits;// 博客点击数
	private BlogFrom from;// 博客评论数
	private String summary;// 博客摘要
	private BlogStatus status;// 博客状态 正常/回收站
	private Space space;// 所属空间
	private Date lastModifyDate;// 最后修改日期
	private int comments;// 博客评论数
	private Scope commentScope;// 博客评论范围
	private Integer level;// 博客级别，级别越高将越靠前显示
	private boolean recommend;// 推荐才能上首页
	private Boolean del;// 是否删除(回收站)
	private Editor editor = Editor.HTML;//编辑方式
	private String display;//博客展现内容

	public Blog() {

	}

	public Blog(Integer id) {
		super(id);
	}

	/**
	 * 获取博客和标签的关联关系
	 * 
	 * @return
	 */
	public List<BlogTag> getBlogTags() {
		if (tags.isEmpty()) {
			return Collections.emptyList();
		}
		List<BlogTag> blogTags = new ArrayList<BlogTag>(tags.size());
		for (Tag tag : tags) {
			blogTags.add(new BlogTag(this, tag));
		}
		return blogTags;
	}

	public void addTags(Tag... tags) {
		if (this.tags == null) {
			this.tags = new HashSet<Tag>();
		}
		for (Tag tag : tags) {
			this.tags.add(tag);
		}
	}

	public Boolean getDel() {
		return del;
	}

	public void setDel(Boolean del) {
		this.del = del;
	}

	/**
	 * 博客是否仅自己可见
	 * 
	 * @return
	 */
	public boolean getIsPrivate() {
		return (Scope.PRIVATE.equals(this.scope));
	}

	public boolean isScheduled() {
		return BlogStatus.SCHEDULED.equals(status);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	public BlogCategory getCategory() {
		return category;
	}

	public void setCategory(BlogCategory category) {
		this.category = category;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public BlogFrom getFrom() {
		return from;
	}

	public void setFrom(BlogFrom from) {
		this.from = from;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public BlogStatus getStatus() {
		return status;
	}

	public void setStatus(BlogStatus status) {
		this.status = status;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public Scope getCommentScope() {
		return commentScope;
	}

	public void setCommentScope(Scope commentScope) {
		this.commentScope = commentScope;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public Editor getEditor() {
		return editor;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	@Override
	public String toString() {
		return "Blog [title=" + title + ", content=" + content + ", writeDate=" + writeDate + ", category=" + category
				+ ", scope=" + scope + ", tags=" + tags + ", hits=" + hits + ", from=" + from + ", summary=" + summary
				+ ", status=" + status + ", space=" + space + ", lastModifyDate=" + lastModifyDate + ", comments="
				+ comments + ", commentScope=" + commentScope + ", level=" + level + ", recommend=" + recommend + "]";
	}

}
