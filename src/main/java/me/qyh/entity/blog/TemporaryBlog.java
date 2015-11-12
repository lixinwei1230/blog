package me.qyh.entity.blog;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.qyh.entity.Id;
import me.qyh.entity.Space;
import me.qyh.helper.htmlclean.JsonHtmlXssSerializer;

/**
 * 临时博客 用户写博客、更新博客和处理临时博客时每隔一段时间会保存临时博客
 * 
 * @author mhlx
 *
 */
public class TemporaryBlog extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String content;// 内容
	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String title;// 标题
	private Date saveDate;// 保存日期
	private Space space;// 所属空间
	private String json;// 博客一些其他信息
	private Blog blog;// 博客

	public TemporaryBlog() {

	}

	public Blog toBlog(ObjectReader reader) throws IOException {
		JsonParser parser = reader.getFactory().createParser(json);
		Blog blog = reader.readValue(parser, Blog.class);
		blog.setTitle(title);
		blog.setContent(content);
		if (this.blog != null) {
			blog.setId(this.blog.getId());
		}
		return blog;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

}
