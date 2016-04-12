package com.javaleo.systems.botmaker.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.model.IEntityBasic;

import com.javaleo.systems.botmaker.ejb.enums.ScriptType;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = "snippet")
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "snippet_sq", sequenceName = "snippet_seq", initialValue = 1, allocationSize = 1)
public class Snippet implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Company company;
	private String name;
	private String description;
	private String regularExpression;
	private ScriptType scriptType;
	private String scriptCode;

	@Override
	@Id
	@Column(name = "snippet_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snippet_sq")
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
	@JoinColumn(name = "company_id", referencedColumnName = "company_id", foreignKey = @ForeignKey(name = "fk_snippet_code_company"), nullable = true)
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

	@Column(name = "regular_expression", length = 180)
	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "script_type", length = 30, nullable = false)
	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	@Column(name = "script_code", columnDefinition = "text")
	public String getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	@Transient
	public String codeResume() {
		return (scriptType.equals(ScriptType.REGEXP)) ? regularExpression : StringUtils.abbreviate(scriptCode, 100);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((regularExpression == null) ? 0 : regularExpression.hashCode());
		result = prime * result + ((scriptCode == null) ? 0 : scriptCode.hashCode());
		result = prime * result + ((scriptType == null) ? 0 : scriptType.hashCode());
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
		Snippet other = (Snippet) obj;
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
		if (regularExpression == null) {
			if (other.regularExpression != null)
				return false;
		} else if (!regularExpression.equals(other.regularExpression))
			return false;
		if (scriptCode == null) {
			if (other.scriptCode != null)
				return false;
		} else if (!scriptCode.equals(other.scriptCode))
			return false;
		if (scriptType != other.scriptType)
			return false;
		return true;
	}

}
