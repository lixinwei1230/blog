package me.qyh.mybatis.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.type.JdbcType;

import me.qyh.entity.Role;
import me.qyh.entity.Role.RoleEnum;
import me.qyh.exception.SystemException;
import me.qyh.utils.Validators;

public class RolesTypeHandler extends DirectResultTypeHandler<List<Role>>{

	@Override
	public List<Role> getNullableResult(ResultSet rs, String str)
			throws SQLException {
		return toRoles(rs.getString(str));
	}

	@Override
	public List<Role> getNullableResult(ResultSet rs, int i)
			throws SQLException {
		return toRoles(rs.getString(i));
	}

	@Override
	public void setNonNullParameter(PreparedStatement arg0, int arg1,
			List<Role> arg2, JdbcType arg3) throws SQLException {
		throw new SystemException("不支持这个方法");
	}
	
	/**
	 * null == > empty list <br/>
	 * ROLE_USER_1 ==> Role{id:1,name:ROLE_USER}
	 * 
	 * @param str
	 * @return
	 */
	protected List<Role> toRoles(String str) {
		if(Validators.isEmptyOrNull(str,true)){
			return Collections.emptyList();
		}
		String [] roleDess = str.split(",");
		List<Role> roles = new ArrayList<Role>(roleDess.length);
		for(String roleDes : roleDess){
			String [] splited = roleDes.split("_");
			Role role = new Role();
			role.setId(Integer.parseInt(splited[2]));
			role.setRole(RoleEnum.valueOf(splited[0]+"_"+splited[1]));
			roles.add(role);
		}
		return roles;
	}

}
