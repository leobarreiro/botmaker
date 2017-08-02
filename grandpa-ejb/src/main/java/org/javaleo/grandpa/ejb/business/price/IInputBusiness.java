package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.price.Input;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IInputBusiness {

	void saveInput(Input input) throws BusinessException;

	List<Input> listAllInputs();

	List<Input> listAllActiveInputs();

	void deactivateInput(Input input) throws BusinessException;

}
