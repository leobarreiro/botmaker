package com.javaleo.systems.botmaker.ejb.enums;

public enum ScriptType {

	NONE("None"), GROOVY("Groovy");

	private ScriptType(String descriptor) {
		this.descriptor = descriptor;
	}

	private String descriptor;

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

}
