package com.javaleo.systems.botmaker.ejb.enums;

public enum AnswerType {

	STRING("The expected answer is a String"),
	INTEGER("An Integer are expected as the answer."),
	DATE("A Date format, with year (yyyy), month (MM) and day (dd) (in any order)"),
	DECIMAL("The expected answer is a number, with one or more decimal places.");

	private AnswerType(String description) {
		this.description = description;
	}

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
