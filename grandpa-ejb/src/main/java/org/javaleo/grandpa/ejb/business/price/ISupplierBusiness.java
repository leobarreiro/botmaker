package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.price.Supplier;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface ISupplierBusiness {

	void saveSupplier(Supplier supplier) throws BusinessException;
	
	List<Supplier> listAllSuppliers();
	
	List<Supplier> listAllActiveSuppliers();
	
	void deactivateSupplier(Supplier supplier) throws BusinessException;
	
}
