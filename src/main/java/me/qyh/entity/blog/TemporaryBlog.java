package me.qyh.entity.blog;

import java.util.Date;
import java.util.Set;

import me.qyh.entity.tag.Tag;
import me.qyh.utils.Validators;

import org.springframework.beans.BeanUtils;

public class TemporaryBlog extends Blog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date saveDate;
	private String tagStr;
	private Integer dummyId;
	
	public TemporaryBlog(Blog blog){
		BeanUtils.copyProperties(blog, this);
		Set<Tag> tags = getTags();
		if(!Validators.isEmptyOrNull(tags)){
			StringBuilder sb = new StringBuilder();
			for(Tag tag : tags){
				String tn = tag.getName();
				if(!Validators.isEmptyOrNull(tn, true)){
					sb.append(tn).append(",");
				}
			}
			if(sb.length() > 0){
				sb.deleteCharAt(sb.length()-1);
			}
			this.tagStr = sb.toString();
		}
		this.saveDate = new Date();
	}
	
	public TemporaryBlog(){
		
	}

	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	public String getTagStr() {
		return tagStr;
	}

	public void setTagStr(String tagStr) {
		this.tagStr = tagStr;
	}

	public Integer getDummyId() {
		return dummyId;
	}

	public void setDummyId(Integer dummyId) {
		this.dummyId = dummyId;
	}
}
