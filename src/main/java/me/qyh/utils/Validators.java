package me.qyh.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public final class Validators {

	private static final Pattern UUID_PATTERN = Pattern
			.compile("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");

	private static final Pattern IPADDRESS_PATTERN = Pattern
			.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
					+ "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
					+ "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}" + "|[1-9][0-9]|[0-9]))");

	private static final String TOP_LEVEL_DOMAIN_STR_FOR_WEB_URL = "(?:" + "(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])"
			+ "|(?:biz|b[abdefghijmnorstvwyz])" + "|(?:cat|com|coop|c[acdfghiklmnoruvxyz])" + "|d[ejkmoz]"
			+ "|(?:edu|e[cegrstu])" + "|f[ijkmor]" + "|(?:gov|g[abdefghilmnpqrstuwy])" + "|h[kmnrtu]"
			+ "|(?:info|int|i[delmnoqrst])" + "|(?:jobs|j[emop])" + "|k[eghimnprwyz]" + "|l[abcikrstuvy]"
			+ "|(?:mil|mobi|museum|m[acdeghklmnopqrstuvwxyz])" + "|(?:name|net|n[acefgilopruz])" + "|(?:org|om)"
			+ "|(?:pro|p[aefghklmnrstwy])" + "|qa" + "|r[eosuw]" + "|s[abcdeghijklmnortuvyz]"
			+ "|(?:tel|travel|t[cdfghjklmnoprtvwz])" + "|u[agksyz]" + "|v[aceginu]" + "|w[fs]"
			+ "|(?:xn\\-\\-0zwm56d|xn\\-\\-11b5bs3a9aj6g|xn\\-\\-80akhbyknj4f|xn\\-\\-9t4b11yi5a|xn\\-\\-deba0ad|xn\\-\\-g6w251d|xn\\-\\-hgbk6aj7f53bba|xn\\-\\-hlcj6aya9esc7a|xn\\-\\-jxalpdlp|xn\\-\\-kgbechtv|xn\\-\\-zckzah)"
			+ "|y[etu]" + "|z[amw]))";

	private static final Pattern URL_PATTERN = Pattern
			.compile("((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
					+ "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
					+ "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
					+ "((?:(?:[a-zA-Z0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-zA-Z0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF\\-]{0,64}\\.)+"
					+ TOP_LEVEL_DOMAIN_STR_FOR_WEB_URL + "|(?:(?:25[0-5]|2[0-4]" // or
																					// ip
																					// address
					+ "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]"
					+ "|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]"
					+ "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}" + "|[1-9][0-9]|[0-9])))"
					+ "(?:\\:\\d{1,5})?)" // plus
											// option
											// port
											// number
					+ "(\\/(?:(?:[a-zA-Z0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF\\;\\/\\?\\:\\@\\&\\=\\#\\~"
					+ "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?" + "(?:\\b|$)");

	private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public static boolean validateIp(final CharSequence input) {
		return validate(IPADDRESS_PATTERN, input);
	}

	public static boolean validateEmail(final CharSequence input) {
		return validate(EMAIL_PATTERN, input);
	}

	public static boolean validateUrl(final CharSequence input) {
		return validate(URL_PATTERN, input);
	}

	public static boolean validate(final Pattern pattern, final CharSequence input) {
		return pattern.matcher(input).matches();
	}

	public static boolean isEmptyOrNull(Collection<?> coll) {
		return (coll == null || coll.isEmpty());
	}

	public static boolean isEmptyOrNull(Object[] array) {
		return (array == null || array.length == 0);
	}

	public static boolean isEmptyOrNull(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}

	public static boolean isEmptyOrNull(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String) {
			return ((String) o).isEmpty();
		} else if (o instanceof Collection) {
			if (((Collection<?>) o).isEmpty()) {
				return true;
			}
		} else if (o.getClass().isArray()) {
			if (Array.getLength(o) == 0) {
				return true;
			}
		} else if (o instanceof Map) {
			if (((Map<?, ?>) o).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmptyOrNull(String str, boolean trim) {
		if (str == null) {
			return true;
		}
		if (trim) {
			return str.trim().isEmpty();
		} else {
			return str.isEmpty();
		}
	}

	public static boolean isUUID(final CharSequence uuid) {
		return validate(UUID_PATTERN, uuid);
	}

	public static boolean hasText(String html) {
		return !Jsoup.clean(html, Whitelist.simpleText()).isEmpty();
	}

}
