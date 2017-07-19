package org.javaleo.grandpa.ejb.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.grandpa.ejb.entities.blog.Gallery;
import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "user")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "sq_user", sequenceName = "user_seq", allocationSize = 1, initialValue = 1)
public class User implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String username;
	private String password;
	private String email;
	private Integer idTelegram;
	private Company company;
	private Boolean admin;
	private Boolean active;
	private List<UserPreference> preferences;
	private List<Gallery> galleries;

	@Override
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_user")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", length = 80)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "username", length = 30)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "passphrase", length = 128)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "email", length = 80)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "telegram_id", nullable = true)
	public Integer getIdTelegram() {
		return idTelegram;
	}

	public void setIdTelegram(Integer idTelegram) {
		this.idTelegram = idTelegram;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "company_id", referencedColumnName = "company_id", foreignKey = @ForeignKey(name = "fk_user_company"))
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Column(name = "admin")
	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	public List<UserPreference> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<UserPreference> preferences) {
		this.preferences = preferences;
	}

	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinTable(schema = EntityUtils.SCHEMA, name = "user_gallery", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "user_fk_user")) },
			inverseJoinColumns = { @JoinColumn(name = "gallery_id", referencedColumnName = "gallery_id", foreignKey = @ForeignKey(name = "gallery_fk_gallery")) })
	public List<Gallery> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<Gallery> galleries) {
		this.galleries = galleries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((admin == null) ? 0 : admin.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idTelegram == null) ? 0 : idTelegram.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		User other = (User) obj;
		if (active == null) {
			if (other.active != null) return false;
		} else if (!active.equals(other.active)) return false;
		if (admin == null) {
			if (other.admin != null) return false;
		} else if (!admin.equals(other.admin)) return false;
		if (company == null) {
			if (other.company != null) return false;
		} else if (!company.equals(other.company)) return false;
		if (email == null) {
			if (other.email != null) return false;
		} else if (!email.equals(other.email)) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (idTelegram == null) {
			if (other.idTelegram != null) return false;
		} else if (!idTelegram.equals(other.idTelegram)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (username == null) {
			if (other.username != null) return false;
		} else if (!username.equals(other.username)) return false;
		return true;
	}

}
