package org.javaleo.grandpa.ejb.enums;

public enum ValidatorType {

	SET("Set of Options"), BOOLEAN("Boolean");

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

	public boolean isSetOfOptions() {
		return this.equals(ValidatorType.SET);
	}

}
