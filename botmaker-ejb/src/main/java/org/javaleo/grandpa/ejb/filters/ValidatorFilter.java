package org.javaleo.grandpa.ejb.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.enums.ValidatorType;

public class ValidatorFilter {

	private String name;
	private String description;
	private ValidatorType validatorType;
	private List<ValidatorType> validatorTypeOptions;

	public ValidatorFilter() {
		super();
		this.validatorTypeOptions = new ArrayList<ValidatorType>(Arrays.asList(ValidatorType.values()));
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

	public ValidatorType getValidatorType() {
		return validatorType;
	}

	public void setValidatorType(ValidatorType validatorType) {
		this.validatorType = validatorType;
	}

	public List<ValidatorType> getValidatorTypeOptions() {
		return validatorTypeOptions;
	}

	public void setValidatorTypeOptions(List<ValidatorType> validatorTypeOptions) {
		this.validatorTypeOptions = validatorTypeOptions;
	}

}
