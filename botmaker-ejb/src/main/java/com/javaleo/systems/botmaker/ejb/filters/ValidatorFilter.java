package com.javaleo.systems.botmaker.ejb.filters;

import java.util.Arrays;
import java.util.List;

import com.javaleo.systems.botmaker.ejb.enums.ScriptType;

public class ValidatorFilter {

	private String name;
	private String description;
	private ScriptType scriptType;
	private List<ScriptType> scriptTypeOptions;

	public ValidatorFilter() {
		super();
		this.scriptTypeOptions = Arrays.asList(ScriptType.values());
	}

	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ScriptType> getScriptTypeOptions() {
		return scriptTypeOptions;
	}

	public void setScriptTypeOptions(List<ScriptType> scriptTypeOptions) {
		this.scriptTypeOptions = scriptTypeOptions;
	}

}
