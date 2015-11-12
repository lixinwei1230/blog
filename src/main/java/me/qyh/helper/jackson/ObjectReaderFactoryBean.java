package me.qyh.helper.jackson;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectReader;

@Component("objectReader")
public class ObjectReaderFactoryBean implements FactoryBean<ObjectReader> {

	@Autowired
	private MyObjectMapper objectMapper;

	@Override
	public ObjectReader getObject() throws Exception {
		return objectMapper.reader();
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectReader.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
