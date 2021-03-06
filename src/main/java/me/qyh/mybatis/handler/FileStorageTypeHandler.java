package me.qyh.mybatis.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;

import me.qyh.exception.SystemException;
import me.qyh.upload.server.FileServer;
import me.qyh.upload.server.FileStorage;
import me.qyh.web.SpringContextHolder;

public class FileStorageTypeHandler extends DirectResultTypeHandler<FileStorage> {
	
	private FileServer fileServer = null;

	@Override
	public FileStorage getNullableResult(ResultSet rs, String str) throws SQLException {
		int id = rs.getInt(str);
		return rs.wasNull() ? null : getStorage(id);
	}

	@Override
	public FileStorage getNullableResult(ResultSet rs, int index) throws SQLException {
		int id = rs.getInt(index);
		return rs.wasNull() ? null : getStorage(id);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int index, FileStorage store, JdbcType type)
			throws SQLException {
		ps.setInt(index, store.id());
	}
	
	private FileStorage getStorage(int id){
		if(fileServer == null){
			fileServer = SpringContextHolder.getBean(FileServer.class);
		}
		FileStorage store = fileServer.getStore(id);
		if(store == null){
			throw new SystemException(String.format("无法找到%s找到FileStorage", id));
		}
		return store;
	}

}
