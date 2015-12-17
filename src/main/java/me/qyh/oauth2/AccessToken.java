package me.qyh.oauth2;

public class AccessToken {

	private String token;// token
	private long expireIn;// 过期时间(s)
	private String refreshToken;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(long expireIn) {
		this.expireIn = expireIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "AccessToken [token=" + token + ", expireIn=" + expireIn + ", refreshToken=" + refreshToken + "]";
	}

}
