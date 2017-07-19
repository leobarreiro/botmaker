package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.price.Product;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IProductBusiness {

	void saveProduct(Product product) throws BusinessException;
	
	List<Product> listAllProducts();
	
	List<Product> listAllActiveProducts();
	
	void deactivateProduct(Product product) throws BusinessException;
	
}
