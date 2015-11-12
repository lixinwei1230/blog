package me.qyh.pageparam;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import me.qyh.bean.Scopes;
import me.qyh.entity.Space;
import me.qyh.entity.blog.BlogCategory;
import me.qyh.entity.blog.BlogFrom;
import me.qyh.entity.blog.BlogStatus;

public class BlogPageParam extends PageParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Space space;
	private String title;
	private Scopes scopes;
	private BlogCategory category;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date end;
	private Set<Integer> tagIds = new HashSet<Integer>();
	private BlogFrom from;
	private BlogStatus status = BlogStatus.NORMAL;
	private boolean ignoreLevel;// 忽略博客的排序值
	private Boolean recommend;

	public BlogPageParam() {

	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Scopes getScopes() {
		return scopes;
	}

	public void setScopes(Scopes scopes) {
		this.scopes = scopes;
	}

	public BlogCategory getCategory() {
		return category;
	}

	public void setCategory(BlogCategory category) {
		this.category = category;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Set<Integer> getTagIds() {
		return tagIds;
	}

	public void setTagIds(Set<Integer> tagIds) {
		this.tagIds = tagIds;
	}

	public BlogFrom getFrom() {
		return from;
	}

	public void setFrom(BlogFrom from) {
		this.from = from;
	}

	public BlogStatus getStatus() {
		return status;
	}

	public void setStatus(BlogStatus status) {
		this.status = status;
	}

	public boolean isIgnoreLevel() {
		return ignoreLevel;
	}

	public void setIgnoreLevel(boolean ignoreLevel) {
		this.ignoreLevel = ignoreLevel;
	}

	public Boolean getRecommend() {
		return recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

}
