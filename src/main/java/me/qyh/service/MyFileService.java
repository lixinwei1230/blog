package me.qyh.service;

import java.util.List;

import me.qyh.entity.MyFile;
import me.qyh.bean.DateFileIndexs;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.pageparam.Page;

public interface MyFileService {

	Page<MyFile> findMyFiles(MyFilePageParam param);

	List<DateFileIndexs> findIndexs(MyFilePageParam param);

	void deleteMyFile(Integer id) throws LogicException;
	
	MyFile getMyFile(Integer id) throws LogicException;
}
