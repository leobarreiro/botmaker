package org.javaleo.grandpa.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "user_prefs")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "sq_prefs", sequenceName = "user_prefs_seq", allocationSize = 1, initialValue = 1)
public class UserPreference implements IEntityBasic {

	public enum PrefsType {
		STRING, INTEGER, FLOAT, BOOLEAN;
	}

	private static final long serialVersionUID = 6604293256169464271L;

	private Long id;
	private String property;
	private PrefsType type;
	private String value;
	private User user;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_prefs")
	@Column(name = "user_prefs_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "property", length = 40, nullable = false)
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "prefs_type", length = 20)
	public PrefsType getType() {
		return type;
	}

	public void setType(PrefsType type) {
		this.type = type;
	}

	@Column(name = "value", columnDefinition = "text")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne()
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		UserPreference other = (UserPreference) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (type != other.type)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
