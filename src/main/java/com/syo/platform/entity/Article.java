package com.syo.platform.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_monitor_article")
public class Article {

	@Id
	@GeneratedValue(generator="uuidGen")
	@GenericGenerator(name="uuidGen", strategy="uuid")
	@Column(length=50)
	private String id;
	
	@Column(length=20)
	private String tag;//故障编号
	
	@Column(length=100)
	private String title;//题目，可以是故障问题
	
	@Column(length=500)
	private String summary;//摘要描述
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String content;//内容，可以是解决办法
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String contentResource;//内容，可以是解决办法
	
	@ManyToOne
	@JoinColumn(name="author")
	private User author;
	
	@Column(length=5)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime = new Date();
	
	@Column(length=500)
	private String vagueTags;
	
	@OneToMany(mappedBy="article", cascade=CascadeType.ALL)
	private List<ArticleAttachment> attachments = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", tag=" + tag + ", title=" + title + ", summary=" + summary + ", author=" + author
				+ ", status=" + status + ", createTime=" + createTime + "]";
	}

	public List<ArticleAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ArticleAttachment> attachments) {
		this.attachments = attachments;
	}

	public String getContentResource() {
		return contentResource;
	}

	public void setContentResource(String contentResource) {
		this.contentResource = contentResource;
	}

	public String getVagueTags() {
		return vagueTags;
	}

	public void setVagueTags(String vagueTags) {
		this.vagueTags = vagueTags;
	}
	
}
