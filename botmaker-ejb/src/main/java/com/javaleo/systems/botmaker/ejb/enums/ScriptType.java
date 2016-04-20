package com.javaleo.systems.botmaker.ejb.enums;

public enum ScriptType {

	SET("Set of Options"), REGEXP("Simple Regexp"), GROOVY("Groovy"), JAVASCRIPT("Javascript"), PYTHON("Python");

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

	public boolean isScript() {
		return (!this.equals(ScriptType.REGEXP) && !this.equals(ScriptType.SET));
	}

	public boolean isSetOfOptions() {
		return this.equals(ScriptType.SET);
	}

}
