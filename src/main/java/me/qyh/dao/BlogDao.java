package me.qyh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.bean.BlogCategoryFile;
import me.qyh.bean.BlogFilesQueryBean;
import me.qyh.bean.DateFileIndex;
import me.qyh.entity.blog.Blog;
import me.qyh.pageparam.BlogPageParam;

public interface BlogDao extends BaseDao<Blog, Integer> {

	int selectCount(BlogPageParam param);

	List<Integer> selectPage(BlogPageParam param);

	int updateHits(@Param("id") Integer id, @Param("hits") int hits);

	List<DateFileIndex> selectDateFile(BlogFilesQueryBean bean);

	List<BlogCategoryFile> selectCategoryFile(BlogFilesQueryBean bean);

	void updateRecommend(Blog blog);
	
	Blog getPreviousBlog(@Param("id") Integer id,@Param("param") BlogPageParam param);
	
	Blog getNextBlog(@Param("id") Integer id,@Param("param") BlogPageParam param);

	int updateComments(@Param("id") Integer blog, @Param("comments") int comments);
	
	void updateDel(Blog blog);
	
	List<Blog> selectByIds(@Param("ids") List<Integer> ids);
	
}
