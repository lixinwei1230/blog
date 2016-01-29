package me.qyh.page.widget.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import me.qyh.utils.Validators;
import me.qyh.web.InvalidParamException;
import me.qyh.web.Webs;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class WidgetConfigResolver implements HandlerMethodArgumentResolver {

	private List<WidgetConfigReader> readers = new ArrayList<WidgetConfigReader>();

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(WidgetConfigAttribute.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		WidgetConfigAttribute att = parameter.getParameterAnnotation(WidgetConfigAttribute.class);

		String sign = webRequest.getParameter(att.sign());
		if (Validators.isEmptyOrNull(sign, true)) {
			throw new InvalidParamException();
		}

		WidgetConfigReader reader = getReader(sign);

		boolean isAjax = Webs.isAjaxRequest(webRequest);
		Class<? extends WidgetConfig> clazz = reader.getConfigClass();
		WidgetConfig config = null;
		WebDataBinder binder = null;
		String name = att.name();
		// 如果是json请求并且需要通过ObjectReader来读取封装对象
		if (isAjax && att.requestBody()) {
			try {
				//need close?
				InputStream is = webRequest.getNativeRequest(HttpServletRequest.class).getInputStream();
				config = Webs.readValue(clazz, is);
				binder = binderFactory.createBinder(webRequest, config, name);
			} catch (IOException e) {
				throw new HttpMessageNotReadableException(e.getMessage(), e);
			}
		} else {
			config = BeanUtils.instantiateClass(clazz);
			// 调用处理器
			binder = binderFactory.createBinder(webRequest, config, name);
			bind(binder, webRequest);
			if (binder.getBindingResult().hasErrors()) {
				throw new BindException(binder.getBindingResult());
			}
		}
		if (att.validate()) {
			binder.validate();
			if (isAjax && binder.getBindingResult().hasErrors()) {
				throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
			}
			return new BindingResultWidgetConfig(binder.getBindingResult(), config);
		}
		return config;
	}

	private WidgetConfigReader getReader(String sign) {
		for (WidgetConfigReader reader : readers) {
			if (reader.match(sign)) {
				return reader;
			}
		}
		throw new InvalidParamException();
	}

	private void bind(WebDataBinder binder, NativeWebRequest request) {
		if (binder instanceof ServletRequestDataBinder) {
			((ServletRequestDataBinder) binder).bind(request.getNativeRequest(HttpServletRequest.class));
		} else if (binder instanceof WebRequestDataBinder) {
			((WebRequestDataBinder) binder).bind(request);
		}
	}

	public void setReaders(List<WidgetConfigReader> readers) {
		this.readers = readers;
	}
}
