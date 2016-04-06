package com.javaleo.systems.botmaker.ejb.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public enum AnswerType {

	STRING("[a-zA-Z0-9]{1,}\\W*[a-zA-Z0-9]{1,}\\W*", "Any string"),
	INTEGER("^[0-9]{1,}$", "A natural integer."),
	DATE("^([0-9]{4}\\W{1}[0-9]{2}\\W{1}[0-9]{2}|[0-9]{2}\\W{1}[0-9]{2}\\W{1}[0-9]{4})$", "A date, composed by year (yyyy), month (MM) and day (dd) (in any order)"),
	DECIMAL("^[0-9]{0,}(\\.|\\,){1}[0-9]{0,}$", "A number, with one or more decimal places."),
	SET("^(%OPTIONS%){1}$", "A set previously defined options.");

	private AnswerType(String pattern, String description) {
		this.description = description;
		this.pattern = pattern;
	}

	private String description;
	private String pattern;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Pattern getSimplePattern() {
		return Pattern.compile(this.pattern);
	}

	public Pattern getArrayPattern(String options) {
		StringBuffer str = new StringBuffer();
		options = StringUtils.lowerCase(options);
		Pattern correctPattern = Pattern.compile("\\s*\\,{1}\\s*");
		Matcher m = correctPattern.matcher(options);
		while (m.find()) {
			m.appendReplacement(str, m.group(1) + "|");
		}
		m.appendTail(str);
		String regexp = StringUtils.replace(this.getPattern(), "%OPTIONS%", str.toString());
		Pattern pattern = Pattern.compile(regexp);
		return pattern;
	}

}
