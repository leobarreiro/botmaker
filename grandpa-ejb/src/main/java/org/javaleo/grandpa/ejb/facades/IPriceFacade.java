package org.javaleo.grandpa.ejb.facades;

import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.price.Brand;
import org.javaleo.grandpa.ejb.entities.price.CategoryInput;
import org.javaleo.grandpa.ejb.entities.price.Input;
import org.javaleo.grandpa.ejb.entities.price.Measure;
import org.javaleo.grandpa.ejb.entities.price.Product;
import org.javaleo.grandpa.ejb.entities.price.Supplier;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IPriceFacade {

	void saveBrand(Brand brand) throws BusinessException;

	List<Brand> listAllBrands();

	List<Brand> listAllActiveBrands();

	void deactivateBrand(Brand brand) throws BusinessException;

	void saveCategoryInput(CategoryInput categoryInput) throws BusinessException;

	List<CategoryInput> listAllCategoryInputs();

	List<CategoryInput> listActiveCategoryInputs();

	void deactivateCategoryInput(CategoryInput categoryInput) throws BusinessException;

	void saveInput(Input input) throws BusinessException;

	List<Input> listAllInputs();

	List<Input> listAllActiveInputs();

	void deactivateInput(Input input) throws BusinessException;

	void saveMeasure(Measure measure) throws BusinessException;

	List<Measure> listAllMeasures();

	List<Measure> listAllActiveMeasures();

	void deactivateMeasure(Measure measure) throws BusinessException;

	void saveProduct(Product product) throws BusinessException;

	List<Product> listAllProducts();

	List<Product> listAllActiveProducts();

	void deactivateProduct(Product product) throws BusinessException;

	void saveSupplier(Supplier supplier) throws BusinessException;

	List<Supplier> listAllSuppliers();

	List<Supplier> listAllActiveSuppliers();

	void deactivateSupplier(Supplier supplier) throws BusinessException;

}
