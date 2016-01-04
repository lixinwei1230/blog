package me.qyh.job;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectReader;

import me.qyh.bean.Info;
import me.qyh.dao.FileDao;
import me.qyh.entity.FileStatus;
import me.qyh.entity.MyFile;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.pageparam.Page;
import me.qyh.upload.server.FileStorage;
import me.qyh.utils.Https;

/**
 * 清理文件的定时任务
 * 
 * @author mhlx
 * 
 */
public class ClearFileJob {

	@Value("${config.pagesize.file}")
	private int pageSize;
	@Autowired
	private FileDao fileDao;
	@Value("${config.tempdir}")
	private String tempdir;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;
	@Autowired
	private ObjectReader reader;

	private Page<MyFile> findMyFiles(MyFilePageParam param) {
		List<MyFile> datas = fileDao.selectPage(param);
		int count = fileDao.selectCount(param);
		return new Page<MyFile>(param, count, datas);
	}

	private void deleteMyFile(MyFile file) throws Exception {
		FileStorage store = file.getStore();
		String url = store.delUrl(file);
		String result = Https.sendPost(url);
		JsonParser parser = reader.getFactory().createParser(result);
		Info info = reader.readValue(parser, Info.class);
		if (info.getSuccess()) {
			MyFile cover = file.getCover();
			if (cover != null) {
				fileDao.deleteById(cover.getId());
			}
			fileDao.deleteById(file.getId());
		}
	}

	public synchronized void doJob() {
		FileUtils.deleteQuietly(new File(tempdir));
		FileUtils.deleteQuietly(new File(imageCacheDir));
		MyFilePageParam param = new MyFilePageParam();
		param.setCurrentPage(1);
		param.setPageSize(pageSize);
		param.setStatus(FileStatus.RECYCLER);
		Page<MyFile> page = findMyFiles(param);
		cleanFiles(page.getDatas());

		int totalPage = page.getTotalPage();
		if (totalPage > 1) {
			for (int i = 2; i <= totalPage; i++) {
				param.setCurrentPage(i);
				cleanFiles(findMyFiles(param).getDatas());
			}
		}
	}

	private void cleanFiles(List<MyFile> files) {
		if (!files.isEmpty()) {
			for (MyFile file : files) {
				try {
					deleteMyFile(file);
				} catch (Exception e) {
				}
			}
		}
	}
}
