package me.qyh.job;

import java.io.File;
import java.util.Date;
import java.util.List;

import me.qyh.dao.FileDao;
import me.qyh.entity.FileStatus;
import me.qyh.entity.MyFile;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.pageparam.Page;
import me.qyh.upload.server.inner.InnerFileUploadServer;
import me.qyh.utils.Times;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
	@Autowired
	private InnerFileUploadServer uploadServer;
	@Value("${config.tempdir}")
	private String tempdir;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;

	private Page<MyFile> findMyFiles(MyFilePageParam param) {
		List<MyFile> datas = fileDao.selectPage(param);
		int count = fileDao.selectCount(param);
		return new Page<MyFile>(param, count, datas);
	}

	private void deleteMyFile(MyFile file) {
		try {
			File _file = uploadServer.seekFile(file.getSeekPath());
			if (_file.exists()) {
				if (!FileUtils.deleteQuietly(_file)) {
					System.gc();
					FileUtils.deleteQuietly(_file);
				}
			}
		} catch (MyFileNotFoundException e) {

		}
	}

	public synchronized void doJob() {
		FileUtils.deleteQuietly(new File(tempdir));
		FileUtils.deleteQuietly(new File(imageCacheDir));
		MyFilePageParam param = new MyFilePageParam();
		param.setCurrentPage(1);
		param.setPageSize(pageSize);
		param.setStatus(FileStatus.RECYCLER);
		Date now = new Date();
		param.setBegin(Times.getPreviousDay(now));
		param.setEnd(Times.getNextDay(now));
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
				deleteMyFile(file);
			}
		}
	}
}
