package com.javaleo.systems.botmaker.ejb.enums;

public enum ValidatorType {

	SET("Set of Options"), REGEXP("Simple Regexp"), GROOVY("Groovy");

	private ValidatorType(String descriptor) {
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
		return (!this.equals(ValidatorType.REGEXP) && !this.equals(ValidatorType.SET));
	}

	public boolean isSetOfOptions() {
		return this.equals(ValidatorType.SET);
	}

}
