package me.qyh.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.qyh.bean.I18NMessage;
import me.qyh.bean.Info;
import me.qyh.exception.BusinessAccessDeinedException;
import me.qyh.exception.LogicException;
import me.qyh.exception.MyFileNotFoundException;
import me.qyh.exception.SpaceDisabledException;
import me.qyh.exception.SystemException;
import me.qyh.web.MyFieldError;
import me.qyh.web.Webs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectWriter;

@ControllerAdvice
public class GolbalExceptionHandler extends BaseController {

	private Logger logger = LoggerFactory.getLogger(GolbalExceptionHandler.class);

	@Autowired
	private ObjectWriter objectWriter;
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(SpaceDisabledException.class)
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView handleSpaceDisabledException(SpaceDisabledException e, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		I18NMessage _message = e.getI18nMessage();
		if (Webs.isAjaxRequest(request)) {
			String message = messageSource.getMessage(_message.getCode(), _message.getParams(), request.getLocale());
			Webs.writeInfo(response, objectWriter, new Info(false, message));
			return null;
		}

		return new ModelAndView("/error/logic").addObject(ERROR, _message);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Info handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
		BindingResult result = ex.getBindingResult();
		List<MyFieldError> errors = new ArrayList<MyFieldError>(result.getFieldErrorCount());
		for (FieldError fieldError : result.getFieldErrors()) {
			String message = messageSource.getMessage(fieldError.getCode(), fieldError.getArguments(),
					request.getLocale());
			MyFieldError error = new MyFieldError(fieldError.getField(), message);
			errors.add(error);
		}
		return new Info(false, errors);
	}

	@ExceptionHandler(MyFileNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView handleMyFileNotFoundException(MyFileNotFoundException e, HttpServletRequest request)
			throws Exception {
		return new ModelAndView("/error/logic").addObject(ERROR, e.getI18nMessage());
	}

	@ExceptionHandler(LogicException.class)
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView handlerLogicException(LogicException e, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		I18NMessage _message = e.getI18nMessage();

		if (Webs.isAjaxRequest(request)) {
			String message = messageSource.getMessage(_message.getCode(), _message.getParams(), request.getLocale());
			Webs.writeInfo(response, objectWriter, new Info(false, message));
			return null;
		}

		return new ModelAndView("/error/logic").addObject(ERROR, _message);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		return new ModelAndView("/error/400");
	}

	@ExceptionHandler(SystemException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleSystemException(SystemException se) throws Exception {
		logger.error(se.getMessage(), se);
		return new ModelAndView("/error/500");
	}
	
	@ExceptionHandler(BusinessAccessDeinedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ModelAndView handleBusinessAccessDeinedException(BusinessAccessDeinedException e) throws IOException {
		return new ModelAndView("/error/403").addObject(ERROR, e.getI18nMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ModelAndView handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex)
			throws IOException {
		return new ModelAndView("/error/405");
	}
}
