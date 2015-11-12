package me.qyh.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 附件组
 * 
 * @author henry.qian
 *
 */
@JsonIgnoreProperties({ "lft", "rgt", "scope" })
public class Node extends Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Node parent;
	private Integer lft;
	private Integer rgt;
	private Integer layer;
	private Date createDate;

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Integer getLft() {
		return lft;
	}

	public void setLft(Integer lft) {
		this.lft = lft;
	}

	public Integer getRgt() {
		return rgt;
	}

	public void setRgt(Integer rgt) {
		this.rgt = rgt;
	}

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public int getScope() {
		return rgt - lft;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Node() {
		super();
	}

	public Node(Integer id) {
		super(id);
	}

}
