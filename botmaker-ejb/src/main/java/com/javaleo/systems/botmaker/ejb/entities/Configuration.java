package com.javaleo.systems.botmaker.ejb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.javaleo.libs.jee.core.model.IEntityBasic;

@Entity
@Table(schema = EntityUtils.SCHEMA, name = EntityUtils.CONFIGURATION)
@SequenceGenerator(schema = EntityUtils.SCHEMA, name = "configuration_sq", sequenceName = "configuration_seq", allocationSize = 1, initialValue = 1)
public class Configuration implements IEntityBasic {

	private static final long serialVersionUID = 1L;

	public enum ConfigurationType {
		INTEGER, NUMERIC, STRING;
	}

	private Long id;
	private String name;
	private ConfigurationType configurationType;
	private String value;

	@Id
	@Column(name = "config_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "configuration_sq")
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

	@Enumerated(EnumType.STRING)
	@Column(name = "config_type", length = 20, nullable = false)
	public ConfigurationType getConfigurationType() {
		return configurationType;
	}

	public void setConfigurationType(ConfigurationType configurationType) {
		this.configurationType = configurationType;
	}

	@Column(name = "value", length = 255, nullable = false)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
