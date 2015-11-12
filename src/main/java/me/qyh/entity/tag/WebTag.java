package me.qyh.entity.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import me.qyh.entity.Id;

public class WebTag extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private Date createDate;
	private List<TagModuleCount> counts = new ArrayList<TagModuleCount>();

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TagModuleCount> getCounts() {
		return counts;
	}

	public void addCounts(TagModuleCount... counts) {
		Collections.addAll(this.counts, counts);
	}

	public WebTag() {

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebTag other = (WebTag) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
