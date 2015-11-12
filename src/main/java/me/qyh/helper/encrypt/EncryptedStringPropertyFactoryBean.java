package me.qyh.helper.encrypt;

import org.springframework.beans.factory.FactoryBean;

public class EncryptedStringPropertyFactoryBean implements FactoryBean<String> {

	private String encryptedProperty;
	private PropertyDecoder decoder;

	@Override
	public String getObject() throws Exception {
		return decoder.decode(encryptedProperty);
	}

	@Override
	public Class<?> getObjectType() {
		return String.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setEncryptedProperty(String encryptedProperty) {
		this.encryptedProperty = encryptedProperty;
	}

	public void setDecoder(PropertyDecoder decoder) {
		this.decoder = decoder;
	}
}
