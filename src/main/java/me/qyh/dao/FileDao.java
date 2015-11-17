package me.qyh.dao;

import java.util.List;

import me.qyh.entity.MyFile;
import me.qyh.bean.DateFileIndex;
import me.qyh.entity.User;
import me.qyh.pageparam.MyFilePageParam;

public interface FileDao extends BaseDao<MyFile, Integer> {

	List<MyFile> selectPage(MyFilePageParam param);

	int selectCount(MyFilePageParam param);

	long selectAllFileSize(User user);

	List<DateFileIndex> selectIndexs(MyFilePageParam param);

}
