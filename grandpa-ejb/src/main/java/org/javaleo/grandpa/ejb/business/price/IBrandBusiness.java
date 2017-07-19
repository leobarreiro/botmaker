package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.price.Brand;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IBrandBusiness {

	void saveBrand(Brand brand) throws BusinessException;
	
	List<Brand> listAllBrands();
	
	List<Brand> listAllActiveBrands();
	
	void deactivateBrand(Brand brand) throws BusinessException;
	
}
