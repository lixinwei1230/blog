package me.qyh.page.widget.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import me.qyh.bean.BlogDateFile;
import me.qyh.bean.BlogFilesQueryBean;
import me.qyh.dao.BlogDao;
import me.qyh.entity.RoleEnum;
import me.qyh.entity.User;
import me.qyh.helper.freemaker.WebFreemarkers;
import me.qyh.page.widget.config.WidgetConfig;
import me.qyh.page.widget.support.BlogDateFiles.DateType;

/**
 * 博客分类归档
 * 
 * @author henry.qian
 *
 */
public class BlogDateFileWidgetHandler
		extends AbstractSimpleSystemWidgetHandler {

	@Autowired
	private BlogDao blogDao;

	public BlogDateFileWidgetHandler(Integer id, String name) {
		super(id, name);
	}

	@Override
	public boolean doAuthencation(User current) {
		return current.hasRole(RoleEnum.ROLE_SPACE);
	}

	@Override
	public WidgetConfig getDefaultWidgetConfig(User current) {
		return new WidgetConfig();
	}

	@Override
	String getPreviewHtml(User user, WebFreemarkers freeMarkers) {
		return parseHtml(user, user, freeMarkers);
	}

	@Override
	String getWidgetHtml(WidgetConfig config, User owner, User visitor,
			WebFreemarkers freeMarkers) {
		return parseHtml(owner, visitor, freeMarkers);
	}

	protected String parseHtml(User user, User visitor,
			WebFreemarkers freeMarkers) {
		BlogFilesQueryBean bean = new BlogFilesQueryBean();
		bean.setSpace(user.getSpace());
		bean.setScopes(userServer.userRelationship(user, visitor));

		List<BlogDateFile> files = blogDao.selectDateFile(bean);
		List<BlogDateFiles> _files = null;
		if (!files.isEmpty()) {
			Map<Integer, List<BlogDateFiles>> filesMap = new HashMap<Integer, List<BlogDateFiles>>();
			for (BlogDateFile file : files) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(file.getDate());
				int year = cal.get(Calendar.YEAR);
				if (filesMap.containsKey(year)) {
					filesMap.get(year)
							.add(new BlogDateFiles(file, DateType.MONTH));
				} else {
					List<BlogDateFiles> subfiles = new ArrayList<BlogDateFiles>();
					subfiles.add(new BlogDateFiles(file, DateType.MONTH));
					filesMap.put(year, subfiles);
				}
			}
			_files = new ArrayList<BlogDateFiles>(filesMap.size());
			for (Map.Entry<Integer, List<BlogDateFiles>> data : filesMap
					.entrySet()) {
				int year = data.getKey();
				BlogDateFile _file = new BlogDateFile();
				int count = 0;
				List<BlogDateFiles> value = data.getValue();
				Collections.sort(value, new Comparator<BlogDateFiles>() {

					@Override
					public int compare(BlogDateFiles o1, BlogDateFiles o2) {
						return o1.getBegin().compareTo(o2.getBegin());
					}

				});
				for (BlogDateFiles datas : data.getValue()) {
					count += datas.getCount();
				}
				_file.setCount(count);
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, year);
				_file.setDate(cal.getTime());
				BlogDateFiles yfs = new BlogDateFiles(_file, DateType.YEAR);
				yfs.setSubfiles(value);
				_files.add(yfs);
			}
			Collections.sort(_files, new Comparator<BlogDateFiles>() {

				@Override
				public int compare(BlogDateFiles o1, BlogDateFiles o2) {
					return -(o1.getBegin().compareTo(o2.getBegin()));
				}

			});
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("files", _files == null ? Collections.emptyList() : _files);
		map.put("widget", super.getSimpleWidget());
		map.put("user", user);

		return freeMarkers.processTemplateIntoString(
				"page/widget/widget_blog_file_date.ftl", map);
	}

}
