package me.qyh.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.bean.DateFileIndex;
import me.qyh.bean.DateFileIndexs;
import me.qyh.bean.Info;
import me.qyh.dao.FileDao;
import me.qyh.entity.MyFile;
import me.qyh.entity.MyFile.FileStatus;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.pageparam.Page;
import me.qyh.service.MyFileService;
import me.qyh.upload.server.FileStorage;
import me.qyh.utils.Times;

@Service("myFileService")
public class MyFileServiceImpl extends BaseServiceImpl implements MyFileService {

	@Autowired
	protected FileDao fileDao;

	@Override
	@Transactional(readOnly = true)
	public Page<MyFile> findMyFiles(MyFilePageParam param) {
		return new Page<MyFile>(param, fileDao.selectCount(param), fileDao.selectPage(param));
	}

	@Override
	@Transactional(readOnly = true)
	public List<DateFileIndexs> findIndexs(MyFilePageParam param) {
		if (param.getBegin() == null || param.getEnd() == null) {
			Date now = new Date();
			param.setBegin(Times.getFirstDayOfThisYear(now));
			param.setEnd(now);
		}
		List<DateFileIndex> indexs =  fileDao.selectIndexs(param);
		return DateFileIndexs.buildYM(indexs);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteMyFile(Integer id) throws LogicException {
		MyFile db = fileDao.selectById(id);
		if (db == null || FileStatus.RECYCLER.equals(db.getStatus())) {
			throw new LogicException("error.file.notexists");
		}

		if (db.getIsCover()) {
			throw new LogicException("error.file.canNotDeleteCover");
		}
		deleteFile(db);
	}
	
	@Override
	@Transactional(readOnly = true)
	public MyFile getMyFile(Integer id) throws LogicException {
		MyFile db = fileDao.selectById(id);
		if (db == null) {
			throw new LogicException("error.file.notexists");
		}
		return db;
	}

	protected void deleteFile(MyFile db) {
		FileStorage storage = db.getStore();
		Info info = storage.del(db);
		if (info.getSuccess()) {
			fileDao.deleteById(db.getId());
		} else {
			db.setStatus(FileStatus.RECYCLER);
			fileDao.update(db);
		}
		MyFile cover = db.getCover();
		if (cover != null) {
			deleteFile(cover);
		}
	}
}
