package com.javaleo.systems.botmaker.ejb.filters;

import java.util.Arrays;
import java.util.List;

import com.javaleo.systems.botmaker.ejb.enums.SnippetType;

public class SnippetFilter {

	private String name;
	private String description;
	private SnippetType scriptType;
	private List<SnippetType> scriptTypeOptions;

	public SnippetFilter() {
		super();
		this.scriptTypeOptions = Arrays.asList(SnippetType.values());
	}

	public SnippetType getScriptType() {
		return scriptType;
	}

	public void setScriptType(SnippetType scriptType) {
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

	public List<SnippetType> getScriptTypeOptions() {
		return scriptTypeOptions;
	}

	public void setScriptTypeOptions(List<SnippetType> scriptTypeOptions) {
		this.scriptTypeOptions = scriptTypeOptions;
	}

}
