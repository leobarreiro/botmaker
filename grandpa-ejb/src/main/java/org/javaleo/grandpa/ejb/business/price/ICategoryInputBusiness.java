package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.price.CategoryInput;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface ICategoryInputBusiness {

	void saveCategoryInput(CategoryInput categoryInput) throws BusinessException;
	
	List<CategoryInput> listAllCategoryInputs();
	
	List<CategoryInput> listActiveCategoryInputs();
	
	void deactivateCategoryInput(CategoryInput categoryInput) throws BusinessException;
	
}
