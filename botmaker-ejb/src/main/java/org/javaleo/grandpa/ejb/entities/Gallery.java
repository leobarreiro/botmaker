package org.javaleo.grandpa.ejb.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(
		schema = EntityUtils.SCHEMA,
		name = "gallery",
		uniqueConstraints = { @UniqueConstraint(
				name = "gallery_uk_uuid",
				columnNames = { "uuid" }) })
@SequenceGenerator(
		schema = EntityUtils.SCHEMA,
		name = "gallery_sq",
		sequenceName = "gallery_seq",
		allocationSize = 1,
		initialValue = 1)
public class Gallery implements IEntityBasic {

	private static final long serialVersionUID = 3287048967214037642L;

	private Long id;
	private String uuid;
	private String name;
	private String description;
	private Date created;
	private Date lastModified;
	private User owner;
	private List<User> viewers;

	@Id
	@Column(
			name = "gallery_id")
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "gallery_sq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(
			name = "uuid",
			length = 32)
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(
			name = "name",
			length = 128,
			nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(
			name = "description",
			length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(
			name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(
			name = "modified")
	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@ManyToOne(
			optional = false)
	@JoinColumn(
			name = "owner_id",
			referencedColumnName = "user_id",
			foreignKey = @ForeignKey(
					name = "gallery_fk_owner"))
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToMany(
			mappedBy = "galleries")
	public List<User> getViewers() {
		return viewers;
	}

	public void setViewers(List<User> viewers) {
		this.viewers = viewers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Gallery other = (Gallery) obj;
		if (created == null) {
			if (other.created != null) return false;
		} else if (!created.equals(other.created)) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (!description.equals(other.description)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (lastModified == null) {
			if (other.lastModified != null) return false;
		} else if (!lastModified.equals(other.lastModified)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (uuid == null) {
			if (other.uuid != null) return false;
		} else if (!uuid.equals(other.uuid)) return false;
		return true;
	}

}
