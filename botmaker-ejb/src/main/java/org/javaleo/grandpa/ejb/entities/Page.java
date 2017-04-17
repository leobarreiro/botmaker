package org.javaleo.grandpa.ejb.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.PAGE)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "page_sq", sequenceName = "page_seq", allocationSize = 1, initialValue = 1)
public class Page implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private String uid;
	private String content;
	private Date created;
	private Date edited;
	private User user;
	private Company company;
	private Category category;
	private Boolean published;
	private Boolean active;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_sq")
	@Column(name = "page_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "title", length = 128)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "uid", length = 128)
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(name = "content", columnDefinition = "text")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "edited")
	public Date getEdited() {
		return edited;
	}

	public void setEdited(Date edited) {
		this.edited = edited;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_page_user"))
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "company_id", referencedColumnName = "company_id", foreignKey = @ForeignKey(name = "fk_page_company"))
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "category_id", referencedColumnName = "category_id")
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Column(name = "published", nullable = true)
	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Transient
	public String getResume() {
		return StringUtils.abbreviate(StringUtils.strip(this.content), 90);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((edited == null) ? 0 : edited.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((published == null) ? 0 : published.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Page other = (Page) obj;
		if (active == null) {
			if (other.active != null) return false;
		} else if (!active.equals(other.active)) return false;
		if (category == null) {
			if (other.category != null) return false;
		} else if (!category.equals(other.category)) return false;
		if (company == null) {
			if (other.company != null) return false;
		} else if (!company.equals(other.company)) return false;
		if (content == null) {
			if (other.content != null) return false;
		} else if (!content.equals(other.content)) return false;
		if (created == null) {
			if (other.created != null) return false;
		} else if (!created.equals(other.created)) return false;
		if (edited == null) {
			if (other.edited != null) return false;
		} else if (!edited.equals(other.edited)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (published == null) {
			if (other.published != null) return false;
		} else if (!published.equals(other.published)) return false;
		if (title == null) {
			if (other.title != null) return false;
		} else if (!title.equals(other.title)) return false;
		if (uid == null) {
			if (other.uid != null) return false;
		} else if (!uid.equals(other.uid)) return false;
		if (user == null) {
			if (other.user != null) return false;
		} else if (!user.equals(other.user)) return false;
		return true;
	}

}
