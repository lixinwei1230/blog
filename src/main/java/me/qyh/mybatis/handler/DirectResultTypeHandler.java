package me.qyh.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.qyh.exception.SystemException;

import org.apache.ibatis.type.BaseTypeHandler;

public abstract class DirectResultTypeHandler<T> extends BaseTypeHandler<T>{

	@Override
	public T getResult(ResultSet rs, int columnIndex) throws SQLException {
		return getNullableResult(rs, columnIndex);
	}

	@Override
	public T getResult(ResultSet rs, String columnName) throws SQLException {
		return getNullableResult(rs , columnName);
	}

	@Override
	public T getNullableResult(CallableStatement arg0, int arg1)
			throws SQLException {
		throw new SystemException("不支持这个方法");
	}
	
}
