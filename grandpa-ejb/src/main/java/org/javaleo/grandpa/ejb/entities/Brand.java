package org.javaleo.grandpa.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "brand")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "sq_brand", sequenceName = "seq_brand", allocationSize = 1, initialValue = 1)
public class Brand implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String website;
	private String description;
	private Boolean active;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_brand")
	@Column(name = "brand_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", length = 30, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "website", length = 120, nullable = true)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Column(name = "description", length = 120, nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((website == null) ? 0 : website.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Brand other = (Brand) obj;
		if (active == null) {
			if (other.active != null) return false;
		} else if (!active.equals(other.active)) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (!description.equals(other.description)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (website == null) {
			if (other.website != null) return false;
		} else if (!website.equals(other.website)) return false;
		return true;
	}

}
