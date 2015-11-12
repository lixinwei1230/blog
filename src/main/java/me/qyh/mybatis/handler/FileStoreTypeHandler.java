package me.qyh.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import me.qyh.upload.server.FileServer;
import me.qyh.upload.server.FileStore;
import me.qyh.web.SpringContextHolder;

public class FileStoreTypeHandler extends BaseTypeHandler<FileStore> {

	@Override
	public FileStore getNullableResult(ResultSet rs, String str)
			throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			FileStore store = SpringContextHolder.getBean(FileServer.class)
					.getStore(rs.getInt(str));
			return store == null ? SpringContextHolder.getBean("avatarStore",
					FileStore.class) : store;
		}
	}

	@Override
	public FileStore getNullableResult(ResultSet rs, int index)
			throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			FileStore store = SpringContextHolder.getBean(FileServer.class)
					.getStore(rs.getInt(index));
			return store == null ? SpringContextHolder.getBean("avatarStore",
					FileStore.class) : store;
		}
	}

	@Override
	public FileStore getNullableResult(CallableStatement cs, int index)
			throws SQLException {
		if (cs.wasNull()) {
			return null;
		} else {
			FileStore store = SpringContextHolder.getBean(FileServer.class)
					.getStore(cs.getInt(index));
			return store == null ? SpringContextHolder.getBean("avatarStore",
					FileStore.class) : store;
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int index,
			FileStore store, JdbcType type) throws SQLException {
		ps.setInt(index, store.id());
	}

}
