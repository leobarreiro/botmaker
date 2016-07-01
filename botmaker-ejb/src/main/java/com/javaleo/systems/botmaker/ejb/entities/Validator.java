package com.javaleo.systems.botmaker.ejb.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.model.IEntityBasic;

import com.javaleo.systems.botmaker.ejb.enums.ValidatorType;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "validator")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "validator_sq", sequenceName = "validator_seq", initialValue = 1, allocationSize = 1)
public class Validator implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Company company;
	private String name;
	private String description;
	private ValidatorType validatorType;
	private Script script;

	@Override
	@Id
	@Column(name = "validator_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "validator_sq")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", length = 60, nullable = false)
	public String getName() {
		return name;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "company_id", referencedColumnName = "company_id", foreignKey = @ForeignKey(name = "fk_validator_company"), nullable = true)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 120)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "validator_type", length = 20, nullable = false)
	public ValidatorType getValidatorType() {
		return validatorType;
	}

	public void setValidatorType(ValidatorType validatorType) {
		this.validatorType = validatorType;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH })
	@JoinColumn(name = "script_id", referencedColumnName = "script_id", nullable = true)
	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	@Transient
	public String getShortDescription() {
		return StringUtils.abbreviate(description, 50);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((validatorType == null) ? 0 : validatorType.hashCode());
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
		Validator other = (Validator) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (validatorType != other.validatorType)
			return false;
		return true;
	}

}
