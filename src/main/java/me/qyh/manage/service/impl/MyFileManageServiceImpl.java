package me.qyh.manage.service.impl;

import me.qyh.entity.FileStatus;
import me.qyh.entity.MyFile;
import me.qyh.exception.LogicException;
import me.qyh.manage.service.MyFileManageService;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.service.impl.MyFileServiceImpl;
import me.qyh.utils.Validators;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyFileManageServiceImpl extends MyFileServiceImpl implements MyFileManageService {

	@Autowired
	private TipServer tipServer;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteMyFile(Integer id, TipMessage tipMessage) throws LogicException {
		MyFile db = getMyFile(id);
		
		super.deleteFile(db);
		
		tipMessage.setReceiver(db.getUser());
		tipMessage.setSender(UserContext.getUser());
		tipServer.sendTip(tipMessage);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void clear() {
		MyFilePageParam param = new MyFilePageParam();
		param.setCurrentPage(1);
		param.setPageSize(Integer.MAX_VALUE);
		param.setStatus(FileStatus.RECYCLER);
		List<MyFile> files = fileDao.selectPage(param);
		if(!Validators.isEmptyOrNull(files)){
			for(MyFile file : files){
				deleteFile(file);
			}
		}
	}

}
