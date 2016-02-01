package me.qyh.entity.blog;

import java.util.Date;

import me.qyh.entity.Id;
import me.qyh.entity.Space;
import me.qyh.helper.html.JsonHtmlXssSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 博客分类
 * 
 * @author mhlx
 *
 */
public class BlogCategory extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String name;// 分类名
	private Space space;// 所属空间
	private int order;// 排序值越小越靠前
	private Date createDate;// 创建日期

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
