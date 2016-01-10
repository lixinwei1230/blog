package me.qyh.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import me.qyh.entity.tag.Tag;
import me.qyh.exception.SystemException;
import me.qyh.utils.Validators;

/**
 * tag_name+"_"+tag_id
 * 
 * @author mhlx
 *
 */
public class TagSetTypeHandler extends BaseTypeHandler<Set<Tag>> {

	@Override
	public Set<Tag> getNullableResult(ResultSet rs, String str) throws SQLException {
		if (rs.wasNull()) {
			return Collections.emptySet();
		}
		String tagList = rs.getString(str);
		return toTags(tagList);
	}

	@Override
	public Set<Tag> getNullableResult(ResultSet rs, int pos) throws SQLException {
		if (rs.wasNull()) {
			return Collections.emptySet();
		}
		String tagList = rs.getString(pos);
		return toTags(tagList);
	}

	@Override
	public Set<Tag> getNullableResult(CallableStatement cs, int pos) throws SQLException {
		if (cs.wasNull()) {
			return Collections.emptySet();
		}
		String tagList = cs.getString(pos);
		return toTags(tagList);
	}

	@Override
	public void setNonNullParameter(PreparedStatement arg0, int arg1, Set<Tag> arg2, JdbcType arg3)
			throws SQLException {
		throw new SystemException("不支持这个方法");
	}

	/**
	 * null == > empty set <br/>
	 * a_72,b_73 ==> tag{id:72,name:a},tag{id:73,name:b}
	 * 
	 * @param str
	 * @return
	 */
	protected Set<Tag> toTags(String str) {
		if(Validators.isEmptyOrNull(str)){
			return Collections.emptySet();
		}
		String[] tagDess = str.split(",");
		Set<Tag> tags = new HashSet<Tag>(tagDess.length);
		for (String tagDes : tagDess) {
			String[] tagAtts = tagDes.split("_");
			Tag tag = new Tag();
			tag.setId(Integer.parseInt(tagAtts[1]));
			tag.setName(tagAtts[0]);
			tags.add(tag);
		}
		return tags;
	}

}
