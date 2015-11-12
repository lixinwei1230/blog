package me.qyh.helper.jackson;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Component("objectMapper")
public class MyObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyObjectMapper() {
		super();
		// 忽视bean对象的JsonFilter annotation
		setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
		// 不写入null
		setSerializationInclusion(Include.NON_NULL);
	}

}
