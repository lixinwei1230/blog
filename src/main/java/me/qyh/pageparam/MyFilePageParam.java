package me.qyh.pageparam;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import me.qyh.entity.FileStatus;
import me.qyh.entity.User;

public class MyFilePageParam extends PageParam {

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

}
