package me.qyh.oauth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public final class Oauths {

	private static final String USER_AGENT = "Mozilla/5.0";

	public static String sendHttpsGet(String urlAndParams) throws IOException {
		BufferedReader in = null;
		try {
			URL obj = new URL(urlAndParams);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Charset", "utf-8");
			con.setRequestProperty("contentType", "utf-8");
			in = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "utf-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			return response.toString();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {

			}
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println(sendHttpsGet(
				"https://graph.qq.com/user/get_user_info?oauth_consumer_key=101243309&format=json&access_token=EC158177BA2CE276A059321A99D11F34&openid=544723CC1DF1D62345F4473BE2288A747D2"));
	}

}
