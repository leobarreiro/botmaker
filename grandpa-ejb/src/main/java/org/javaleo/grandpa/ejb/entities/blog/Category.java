package org.javaleo.grandpa.ejb.entities.blog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.EntityUtils;
import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.CATEGORY)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "category_sq", sequenceName = "category_seq", allocationSize = 1, initialValue = 1)
public class Category implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String key;
	private Boolean active;
	private Boolean firstOption;
	private Blog blog;
	private Company company;

	@Id
	@Column(name = "category_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", length = 30)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "category_key", length = 32, nullable = true)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "first_option")
	public Boolean getFirstOption() {
		return firstOption;
	}

	public void setFirstOption(Boolean firstOption) {
		this.firstOption = firstOption;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "blog_id", referencedColumnName = "blog_id")
	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "company_id", referencedColumnName = "company_id")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((blog == null) ? 0 : blog.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((firstOption == null) ? 0 : firstOption.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Category other = (Category) obj;
		if (active == null) {
			if (other.active != null) return false;
		} else if (!active.equals(other.active)) return false;
		if (blog == null) {
			if (other.blog != null) return false;
		} else if (!blog.equals(other.blog)) return false;
		if (company == null) {
			if (other.company != null) return false;
		} else if (!company.equals(other.company)) return false;
		if (firstOption == null) {
			if (other.firstOption != null) return false;
		} else if (!firstOption.equals(other.firstOption)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (key == null) {
			if (other.key != null) return false;
		} else if (!key.equals(other.key)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}

}
