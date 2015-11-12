package me.qyh.bean;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import me.qyh.entity.Space;

/**
 * 博客分类查询对象
 * 
 * @author mhlx
 *
 */
public class BlogFilesQueryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date begin;// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date end;// 结束时间
	private Scopes scopes ;
	private Space space;// 所属空间

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

	public Scopes getScopes() {
		return scopes;
	}

	public void setScopes(Scopes scopes) {
		this.scopes = scopes;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

}
