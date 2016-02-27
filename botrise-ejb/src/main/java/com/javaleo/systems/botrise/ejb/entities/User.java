package com.javaleo.systems.botrise.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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

}
