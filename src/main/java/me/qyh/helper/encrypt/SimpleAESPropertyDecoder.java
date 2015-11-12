package me.qyh.helper.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.util.Base64;

public class SimpleAESPropertyDecoder implements PropertyDecoder {

	private static final String AES = "AES";

	private String charset = "utf-8";

	@Override
	public String decode(String encrypted) throws Exception {
		return this.decrypt(encrypted);
	}

	private byte[] encryptToBytes(byte[] encodedKey, String content)
			throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encodedKey, AES));
		return cipher.doFinal(content.getBytes(charset));
	}

	private String decryptByBytes(String key, byte[] encryptBytes)
			throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		cipher.init(Cipher.DECRYPT_MODE,
				new SecretKeySpec(Base64.decodeBase64(key), AES));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes);
	}

	private String decrypt(String encryptStr) throws Exception {
		String _parsed = new String(Base64.decodeBase64(encryptStr), charset);
		String[] parsed = getKeyAndEncryptedFromMixed(_parsed);
		return decryptByBytes(parsed[0], Base64.decodeBase64(parsed[1]));
	}

	private String encrypt(String content) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(AES);
		kgen.init(128, new SecureRandom());
		byte[] encodedKey = kgen.generateKey().getEncoded();
		String base64d = Base64.encodeBase64String(encodedKey);
		String encrypted = Base64
				.encodeBase64String(encryptToBytes(encodedKey, content));
		return Base64.encodeBase64String(
				mixKeyAndPassword(base64d, encrypted).getBytes(charset));
	}

	protected String mixKeyAndPassword(String key, String password) {
		StringBuilder reversedKey = new StringBuilder().append(key).reverse();
		StringBuilder psb = new StringBuilder(password);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < psb.length(); i++) {
			result.append(psb.charAt(i));
			result.append(reversedKey.charAt(i));
		}
		return result.toString();
	}

	protected String[] getKeyAndEncryptedFromMixed(String mixed) {
		StringBuilder sb = new StringBuilder(mixed);
		StringBuilder key = new StringBuilder();
		StringBuilder pwd = new StringBuilder();
		for (int i = 0; i < mixed.length(); i++) {
			if (i % 2 == 0) {
				pwd.append(sb.charAt(i));
			} else {
				key.append(sb.charAt(i));
			}
		}
		return new String[] { key.reverse().toString(), pwd.toString() };
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public static void main(String[] args) throws Exception {
		SimpleAESPropertyDecoder sd = new SimpleAESPropertyDecoder();
		String content = "root";
		String encrypt = sd.encrypt(content);
		System.out.println("加密后:" + encrypt);
		System.out.println("解密后:" + sd.decode(encrypt));
	}
}
