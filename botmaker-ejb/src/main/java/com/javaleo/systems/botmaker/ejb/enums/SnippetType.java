package com.javaleo.systems.botmaker.ejb.enums;

public enum SnippetType {

	SET("Set of Options"), REGEXP("Simple Regexp"), GROOVY("Groovy"), JAVASCRIPT("Javascript"), PYTHON("Python");

	private SnippetType(String descriptor) {
		this.descriptor = descriptor;
	}

	private String descriptor;

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public boolean isScript() {
		return (!this.equals(SnippetType.REGEXP) && !this.equals(SnippetType.SET));
	}

	public boolean isSetOfOptions() {
		return this.equals(SnippetType.SET);
	}

}
