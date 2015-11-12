package me.qyh.helper.jackson;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectWriter;

@Component("objectWriter")
public class ObjectWriterFactoryBean implements FactoryBean<ObjectWriter> {

	@Autowired
	private MyObjectMapper objectMapper;

	@Override
	public ObjectWriter getObject() throws Exception {
		return objectMapper.writer();
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectWriter.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
