package com.javaleo.systems.botmaker.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.CATEGORY)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "category_sq", sequenceName = "category_seq", allocationSize = 1, initialValue = 1)
public class Category implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
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

	@ManyToOne(optional = false)
	@JoinColumn(name = "company_id", referencedColumnName = "company_id")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
