package me.qyh.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import me.qyh.bean.Info;
import me.qyh.utils.Validators;
import me.qyh.web.tag.url.UrlHelper;

import org.springframework.http.MediaType;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.SerializationUtils;
import org.springframework.web.context.request.NativeWebRequest;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.code.kaptcha.Constants;

public final class Webs {

	public static final String rootPath = System.getProperty("webapp.root");
	private static final String URL_HELPER = "urlHelper";
	private static final String REFERER = "Referer";
	private static final ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	public static ObjectMapper getMapper(){
		return mapper;
	}
	
	public  static <T> T readValue(Class<T> t , String json) throws JsonProcessingException, IOException{
		ObjectReader reader = mapper.reader(t);
		return reader.readValue(json);
	}
	
	public  static <T> T readValue(Class<T> t , InputStream is) throws JsonProcessingException, IOException{
		ObjectReader reader = mapper.reader(t);
		return reader.readValue(is);
	}
	
	public static <T> T readValue(Class<T> t ,URL url) throws JsonProcessingException, IOException{
		ObjectReader reader = mapper.reader(t);
		return reader.readValue(url);
	}
	
	public static ObjectReader reader(){
		return mapper.reader();
	}
	

	public static boolean isAjaxRequest(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	public static boolean isAjaxRequest(NativeWebRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	private static String[] HEADERS_TO_TRY = { "REMOTE_ADDR", "X-Forwarded-For" , "X-Real-IP" };

	public static String getClientIpAddress(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (!Validators.isEmptyOrNull(ip, true) && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

	public static boolean matchValidateCode(HttpSession session, String code) {
		if(session == null){
			return false;
		}
		String _validateCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		if (Validators.isEmptyOrNull(_validateCode, true) || Validators.isEmptyOrNull(code, true)
				|| !_validateCode.equalsIgnoreCase(code)) {
			return false;
		}
		return true;
	}

	public static String getRequestUrl(HttpServletRequest request) {
		return UrlUtils.buildFullRequestUrl(request);
	}

	public static void writeInfo(HttpServletResponse response,  Info info)
			throws IOException {
		response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//		JsonGenerator jsonGenerator = objectWriter.getFactory().createGenerator(response.getOutputStream(),
//				JsonEncoding.UTF8);
		ObjectWriter writer = mapper.writer();
		writer.writeValue(response.getOutputStream(), info);
	}

	public static UrlHelper getUrlHelper(ServletContext sc) {
		return (UrlHelper) sc.getAttribute(URL_HELPER);
	}

	public static String generatorETag(Object o) {
		StringBuilder builder = new StringBuilder("\"0");
		DigestUtils.appendMd5DigestAsHex(SerializationUtils.serialize(o), builder);
		builder.append('"');
		return builder.toString();
	}

	public static boolean isWebImage(String filename) {
		if (!Validators.isEmptyOrNull(filename, false)) {
			String contentType = URLConnection.guessContentTypeFromName(filename);
			if (contentType != null && contentType.startsWith("image")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSafeFilePath(String path) {
		return (!Validators.isEmptyOrNull(path, true) && path.indexOf("..") == -1);
	}

	public static String getReferer(HttpServletRequest request) {
		return request.getHeader(REFERER);
	}
}
