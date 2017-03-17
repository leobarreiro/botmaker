package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.ValidatorFilter;

@Local
public interface IValidatorBusiness extends Serializable {

	void saveValidator(Validator snippetCode) throws BusinessException;

	List<Validator> searchValidatorByFilter(ValidatorFilter filter);

	public List<List<String>> getOptionsByValidator(Validator validator);

	boolean validateContent(Validator validator, String content) throws BusinessException;

}
