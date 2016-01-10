package me.qyh.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.User;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogComment;
import me.qyh.pageparam.CommentPageParam;

public interface BlogCommentDao extends BaseDao<BlogComment, Integer> {

	void deleteByBlog(Blog blog);

	void deleteByParent(BlogComment comment);

	int selectCount(CommentPageParam param);

	List<BlogComment> selectPage(CommentPageParam param);

	int selectCountByDate(@Param("begin") Date begin, @Param("end") Date end, @Param("user") User user);
}
