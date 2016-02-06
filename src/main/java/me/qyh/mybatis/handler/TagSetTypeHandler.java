package me.qyh.mybatis.handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import me.qyh.entity.tag.Tag;
import me.qyh.exception.SystemException;
import me.qyh.utils.Validators;

import org.apache.ibatis.type.JdbcType;

/**
 * tag_name+"_"+tag_id
 * 
 * @author mhlx
 *
 */
public class TagSetTypeHandler extends DirectResultTypeHandler<Set<Tag>> {

	@Override
	public Set<Tag> getNullableResult(ResultSet rs, String str) throws SQLException {
		return toTags(rs.getString(str));
	}

	@Override
	public Set<Tag> getNullableResult(ResultSet rs, int pos) throws SQLException {
		return toTags(rs.getString(pos));
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
		if(Validators.isEmptyOrNull(str,true)){
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
