package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Validator;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.ValidatorFilter;

@Local
public interface IValidatorBusiness extends Serializable {

	void saveValidator(Validator snippetCode) throws BusinessException;

	List<Validator> searchValidatorByFilter(ValidatorFilter filter);

	public List<List<String>> getOptionsByValidator(Validator validator);

	boolean validateContent(Validator validator, String content) throws BusinessException;

}
