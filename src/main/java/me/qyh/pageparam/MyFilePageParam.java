package me.qyh.pageparam;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import me.qyh.entity.MyFile.FileStatus;
import me.qyh.entity.User;

public class MyFilePageParam extends PageParam  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileStatus status;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date end;
	private User user;
	private String name;
	private Boolean showCover;
	private Set<String> extensions = new HashSet<String>();
	private Long small;
	private Long large;
	private Integer store;

	public FileStatus getStatus() {
		return status;
	}

	public void setStatus(FileStatus status) {
		this.status = status;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getShowCover() {
		return showCover;
	}

	public void setShowCover(Boolean showCover) {
		this.showCover = showCover;
	}

	public Set<String> getExtensions() {
		return extensions;
	}

	public void setExtensions(Set<String> extensions) {
		this.extensions = extensions;
	}

	public Long getSmall() {
		return small;
	}

	public void setSmall(Long small) {
		this.small = small;
	}

	public Long getLarge() {
		return large;
	}

	public void setLarge(Long large) {
		this.large = large;
	}

	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}
}
