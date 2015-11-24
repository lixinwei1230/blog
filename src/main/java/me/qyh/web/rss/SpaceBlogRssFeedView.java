package me.qyh.web.rss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;

import me.qyh.entity.Space;
import me.qyh.entity.blog.Blog;
import me.qyh.web.controller.SpaceBlogRssController;
import me.qyh.web.tag.url.UrlHelper;

public class SpaceBlogRssFeedView extends AbstractRssFeedView {

	private static final String BLOGS = SpaceBlogRssController.BLOGS;

	@Autowired
	private UrlHelper helper;

	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
		Space space = (Space) model.get("space");

		feed.setTitle(space.getId());
		feed.setDescription(space.getId());
		feed.setLink(helper.getUrlBySpace(space));

		super.buildFeedMetadata(model, feed, request);
	}

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		@SuppressWarnings("unchecked")
		List<Blog> blogs = (List<Blog>) model.get(BLOGS);

		if (blogs.isEmpty()) {
			return Collections.emptyList();
		}

		Space space = (Space) model.get("space");

		List<Item> items = new ArrayList<Item>(blogs.size());
		for (Blog blog : blogs) {
			Item item = new Item();

			Content content = new Content();
			content.setValue(blog.getSummary());
			item.setContent(content);

			item.setTitle(blog.getTitle());
			item.setLink(helper.getUrlBySpace(space) + "/blog/" + blog.getId());
			item.setPubDate(blog.getWriteDate());

			items.add(item);
		}
		return items;
	}

}
