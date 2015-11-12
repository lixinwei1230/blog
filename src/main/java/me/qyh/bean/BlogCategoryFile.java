package me.qyh.bean;

import me.qyh.entity.blog.BlogCategory;

/**
 * 博客分类归档
 * 
 * @author mhlx
 *
 */
public class BlogCategoryFile {

	private int count;// 某分类下博客数量
	private BlogCategory category;// 博客分类

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BlogCategory getCategory() {
		return category;
	}

	public void setCategory(BlogCategory category) {
		this.category = category;
	}

}
