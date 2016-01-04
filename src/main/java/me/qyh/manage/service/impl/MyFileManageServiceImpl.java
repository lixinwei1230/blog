package me.qyh.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectReader;

import me.qyh.bean.Info;
import me.qyh.entity.MyFile;
import me.qyh.exception.LogicException;
import me.qyh.exception.SystemException;
import me.qyh.manage.service.MyFileManageService;
import me.qyh.security.UserContext;
import me.qyh.server.TipMessage;
import me.qyh.server.TipServer;
import me.qyh.service.impl.MyFileServiceImpl;
import me.qyh.upload.server.FileStorage;
import me.qyh.utils.Https;

@Service
public class MyFileManageServiceImpl extends MyFileServiceImpl implements MyFileManageService {

	@Autowired
	private TipServer tipServer;
	@Autowired
	private ObjectReader reader;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteMyFile(Integer id, TipMessage tipMessage) throws LogicException {
		MyFile db = getMyFile(id);
		MyFile cover = db.getCover();
		deleteFile(db);
		if(cover != null){
			deleteFile(cover);
		}
		tipMessage.setReceiver(db.getUser());
		tipMessage.setSender(UserContext.getUser());
		tipServer.sendTip(tipMessage);
	}

	private void deleteFile(MyFile db) {
		FileStorage storage = db.getStore();
		String url = storage.delUrl(db);
		Info info = new Info(false);
		try {
			String result = Https.sendPost(url);
			JsonParser parser = reader.getFactory().createParser(result);
			info = reader.readValue(parser, Info.class);
		} catch (Exception e) {
			throw new SystemException(e.getMessage(), e);
		}
		if (info.getSuccess()) {
			fileDao.deleteById(db.getId());
		} else {
			super.deleteMyFile(db);
		}
	}

}
