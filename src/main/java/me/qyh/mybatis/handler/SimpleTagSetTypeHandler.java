package me.qyh.mybatis.handler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import me.qyh.entity.tag.Tag;
import me.qyh.utils.Validators;

/**
 * tag_name+"_"+tag_id
 * 
 * @author mhlx
 *
 */
public class SimpleTagSetTypeHandler extends TagSetTypeHandler {

	/**
	 * null == > empty set <br/>
	 * a,b ==> tag{name:a},tag{name:b}
	 */
	@Override
	protected Set<Tag> toTags(String str) {
		if(Validators.isEmptyOrNull(str,true)){
			return Collections.emptySet();
		}
		String [] names = str.split(",");
		Set<Tag> tags = new HashSet<Tag>(names.length);
		for(String name : names){
			Tag tag = new Tag();
			tag.setName(name);
			tags.add(tag);
		}
		return tags;
	}


}
