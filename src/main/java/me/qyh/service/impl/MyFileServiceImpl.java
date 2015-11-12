package me.qyh.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.dao.FileDao;
import me.qyh.entity.FileStatus;
import me.qyh.entity.MyFile;
import me.qyh.entity.MyFileIndex;
import me.qyh.exception.DataNotFoundException;
import me.qyh.exception.LogicException;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.pageparam.Page;
import me.qyh.security.UserContext;
import me.qyh.service.MyFileService;
import me.qyh.utils.Times;

@Service("myFileService")
public class MyFileServiceImpl extends BaseServiceImpl
		implements MyFileService {

	@Autowired
	private FileDao fileDao;

	@Override
	@Transactional(readOnly = true)
	public Page<MyFile> findMyFiles(MyFilePageParam param) {
		return new Page<MyFile>(param, fileDao.selectCount(param),
				fileDao.selectPage(param));
	}

	@Override
	@Transactional(readOnly = true)
	public List<MyFileIndex> findIndexs(MyFilePageParam param) {
		if (param.getBegin() == null || param.getEnd() == null) {
			Date now = new Date();
			param.setBegin(Times.getFirstDayOfThisYear(now));
			param.setEnd(now);
		}
		return fileDao.selectIndexs(param);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,
			rollbackFor = Exception.class)
	public void deleteMyFile(Integer id) throws LogicException {
		MyFile db = fileDao.selectById(id);
		if (db == null || FileStatus.RECYCLER.equals(db.getStatus())) {
			throw new LogicException("error.file.notexists");
		}
		super.doAuthencation(UserContext.getUser(), db.getUser());
		
		if(db.getIsCover()){
			throw new LogicException("error.file.canNotDeleteCover");
		}
		deleteMyFile(db);
		MyFile cover = db.getCover();
		if(cover != null){
			deleteMyFile(cover);
		}
	}

	protected MyFile getMyFile(Integer id) throws DataNotFoundException {
		MyFile db = fileDao.selectById(id);
		if (db == null) {
			throw new DataNotFoundException("error.file.notexists");
		}
		return db;
	}

	protected void deleteMyFile(MyFile db) {
		db.setStatus(FileStatus.RECYCLER);
		fileDao.update(db);
	}

}
