package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.bean.BlogCategoryFile;
import me.qyh.bean.BlogDateFile;
import me.qyh.bean.BlogFilesQueryBean;
import me.qyh.entity.blog.Blog;
import me.qyh.pageparam.BlogPageParam;

public interface BlogDao extends BaseDao<Blog, Integer> {

	int selectCount(BlogPageParam param);

	List<Blog> selectPage(BlogPageParam param);

	int updateHits(@Param("id") Integer blog, @Param("hits") int hits);

	List<BlogDateFile> selectDateFile(BlogFilesQueryBean bean);

	List<BlogCategoryFile> selectCategoryFile(BlogFilesQueryBean bean);

	void updateRecommend(Blog blog);

}
