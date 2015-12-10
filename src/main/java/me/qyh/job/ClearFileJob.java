package me.qyh.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.qyh.dao.FileDao;
import me.qyh.entity.FileStatus;
import me.qyh.entity.MyFile;
import me.qyh.pageparam.MyFilePageParam;
import me.qyh.pageparam.Page;
import me.qyh.upload.server.FileStore;
import me.qyh.utils.Times;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	@Value("${config.tempdir}")
	private String tempdir;
	@Value("${config.image.thumb.cachedir}")
	private String imageCacheDir;

	private static final Logger logger = LoggerFactory
			.getLogger(ClearFileJob.class);

	private Page<MyFile> findMyFiles(MyFilePageParam param) {
		List<MyFile> datas = fileDao.selectPage(param);
		int count = fileDao.selectCount(param);
		return new Page<MyFile>(param, count, datas);
	}

	private void deleteMyFile(MyFile file) {
		FileStore store = file.getStore();
		String url = store.deleteUrl() + "?path=" + file.getSeekPath()
				+ "&key=" + store.delKey();
		sendPost(url , store.protocol());
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

	private String sendPost(String url , String protocol) {
		if ("https".equalsIgnoreCase(protocol)) {
			try {
				trustAllHttpsCertificates();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
			HostnameVerifier hv = new HostnameVerifier() {
				@Override
				public boolean verify(String str, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hv); 
		}
		BufferedReader in = null;
		String result = "";
		HttpURLConnection conn = null; 
		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection)realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
		} finally {
			IOUtils.closeQuietly(in);
			if(conn != null){
				conn.disconnect();
			}
		}
		return result;
	}

	private static void trustAllHttpsCertificates() throws Exception {
		TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements TrustManager, X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				X509Certificate[] certs, String authType)
				throws CertificateException {
			return;
		}

		public void checkClientTrusted(
				X509Certificate[] certs, String authType)
				throws CertificateException {
			return;
		}
	}
}
