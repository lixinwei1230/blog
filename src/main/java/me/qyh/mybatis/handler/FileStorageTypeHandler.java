package me.qyh.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import me.qyh.upload.server.FileServer;
import me.qyh.upload.server.FileStorage;
import me.qyh.web.SpringContextHolder;

public class FileStorageTypeHandler extends BaseTypeHandler<FileStorage> {

	@Override
	public FileStorage getNullableResult(ResultSet rs, String str) throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			FileStorage store = SpringContextHolder.getBean(FileServer.class).getStore(rs.getInt(str));
			return store == null ? SpringContextHolder.getBean("avatarStore", FileStorage.class) : store;
		}
	}

	@Override
	public FileStorage getNullableResult(ResultSet rs, int index) throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			FileStorage store = SpringContextHolder.getBean(FileServer.class).getStore(rs.getInt(index));
			return store == null ? SpringContextHolder.getBean("avatarStore", FileStorage.class) : store;
		}
	}

	@Override
	public FileStorage getNullableResult(CallableStatement cs, int index) throws SQLException {
		if (cs.wasNull()) {
			return null;
		} else {
			FileStorage store = SpringContextHolder.getBean(FileServer.class).getStore(cs.getInt(index));
			return store == null ? SpringContextHolder.getBean("avatarStore", FileStorage.class) : store;
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int index, FileStorage store, JdbcType type)
			throws SQLException {
		ps.setInt(index, store.id());
	}

}
