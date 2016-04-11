package com.javaleo.systems.botmaker.ejb.filters;

import com.javaleo.systems.botmaker.ejb.enums.ScriptType;

public class SnippetCodeFilter {

	private ScriptType scriptType;
	private String name;
	private String description;

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

}
