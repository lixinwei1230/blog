package me.qyh.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import me.qyh.helper.html.JsonHtmlXssSerializer;

/**
 * 空间
 * 
 * @author mhlx
 *
 */
public class Space implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = JsonHtmlXssSerializer.class)
	private String id;
	private User user;
	private Date createDate;// 创建日期
	private SpaceStatus status;// 空间状态

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SpaceStatus getStatus() {
		return status;
	}

	public void setStatus(SpaceStatus status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Space other = (Space) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Space [id=" + id + "]";
	}

	public Space() {
		super();
	}

	public Space(String id) {
		super();
		this.id = id;
	}

}
