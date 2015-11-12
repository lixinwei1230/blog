package me.qyh.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.util.StringUtils;

public final class Strings extends StringUtils {

	/**
	 * MD5加密
	 * 
	 * @param input
	 * @return
	 */
	public static String getMd5(String input) {
		if (input == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean inArray(String str, String[] array, boolean ignoreCase) {
		if (Validators.isEmptyOrNull(array) || Validators.isEmptyOrNull(str, false)) {
			return false;
		}
		for (String arr : array) {
			if (ignoreCase && str.equalsIgnoreCase(arr)) {
				return true;
			} else {
				if (str.equals(arr)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String deleteWhitespace(String str) {
		return org.apache.commons.lang.StringUtils.deleteWhitespace(str);
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
