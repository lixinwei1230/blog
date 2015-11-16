package me.qyh.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import me.qyh.dao.FileDao;
import me.qyh.entity.FileStatus;
import me.qyh.entity.MyFile;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.pageparam.Page;
import me.qyh.upload.server.FileStore;
import me.qyh.utils.Times;

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

	private Page<MyFile> findMyFiles(MyFilePageParam param) {
		List<MyFile> datas = fileDao.selectPage(param);
		int count = fileDao.selectCount(param);
		return new Page<MyFile>(param, count, datas);
	}

	private void deleteMyFile(MyFile file) {
		FileStore store = file.getStore();
		sendPost(store.deleteUrl() + "?path=" + file.getSeekPath() + "&key=" + store.delKey());
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

	private String sendPost(String url) {
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
		} finally {
			IOUtils.closeQuietly(in);
		}
		return result;
	}
}
