package me.qyh.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import me.qyh.entity.Comment;
import me.qyh.entity.CommentScope;
import me.qyh.entity.User;
import me.qyh.pageparam.CommentPageParam;

public interface CommentDao extends BaseDao<Comment, Integer> {

	void deleteByCommentScope(CommentScope target);

	void deleteByParent(Comment comment);

	int selectCount(CommentPageParam param);

	List<Comment> selectPage(CommentPageParam param);

	int selectCountByDate(@Param("begin") Date begin, @Param("end") Date end, @Param("user") User user);
}
