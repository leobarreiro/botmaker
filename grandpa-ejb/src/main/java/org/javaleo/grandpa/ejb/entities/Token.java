package org.javaleo.grandpa.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "token")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "sq_token", sequenceName = "token_seq", allocationSize = 1, initialValue = 1)
public class Token implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private User user;
	private Long validUntil;
	private String uuid;
	private Boolean used;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_token")
	@Column(name = "token_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "valid_until")
	public Long getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Long validUntil) {
		this.validUntil = validUntil;
	}

	@Column(name = "uuid", length = 128)
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "used")
	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((used == null) ? 0 : used.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + ((validUntil == null) ? 0 : validUntil.hashCode());
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
		Token other = (Token) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (used == null) {
			if (other.used != null)
				return false;
		} else if (!used.equals(other.used))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (validUntil == null) {
			if (other.validUntil != null)
				return false;
		} else if (!validUntil.equals(other.validUntil))
			return false;
		return true;
	}

}
