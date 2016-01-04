package me.qyh.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

public final class Https {
	
	public static String sendPost(String url) throws Exception {
		String protocol = url.substring(0,url.indexOf(":"));
		if ("https".equalsIgnoreCase(protocol)) {
			trustAllHttpsCertificates();
			HostnameVerifier hv = new HostnameVerifier() {
				@Override
				public boolean verify(String str, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		}
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
		} finally {
			IOUtils.closeQuietly(in);
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result.toString();
	}

	private static void trustAllHttpsCertificates() throws Exception {
		TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		TrustManager tm = new TrustAll();
		trustAllCerts[0] = tm;
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	private static class TrustAll implements TrustManager, X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
			return;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
			return;
		}
	}

}
