package me.qyh.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import me.qyh.entity.MyFile;
import me.qyh.exception.LogicException;
import me.qyh.manage.service.MyFileManageService;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.service.impl.MyFileServiceImpl;

@Service
public class MyFileManageServiceImpl extends MyFileServiceImpl implements MyFileManageService {

	@Autowired
	private TipServer tipServer;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteMyFile(Integer id, TipMessage tipMessage) throws LogicException {
		MyFile db = getMyFile(id);

		tipMessage.setReceiver(db.getUser());
		tipMessage.setSender(UserContext.getUser());
		tipServer.sendTip(tipMessage);

		super.deleteMyFile(db);
	}

}
