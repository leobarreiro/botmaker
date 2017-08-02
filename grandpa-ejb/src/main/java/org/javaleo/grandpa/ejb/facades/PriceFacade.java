package org.javaleo.grandpa.ejb.facades;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.business.price.IBrandBusiness;
import org.javaleo.grandpa.ejb.business.price.ICategoryInputBusiness;
import org.javaleo.grandpa.ejb.business.price.IInputBusiness;
import org.javaleo.grandpa.ejb.business.price.IMeasureBusiness;
import org.javaleo.grandpa.ejb.business.price.IProductBusiness;
import org.javaleo.grandpa.ejb.business.price.ISupplierBusiness;
import org.javaleo.grandpa.ejb.entities.price.Brand;
import org.javaleo.grandpa.ejb.entities.price.CategoryInput;
import org.javaleo.grandpa.ejb.entities.price.Input;
import org.javaleo.grandpa.ejb.entities.price.Measure;
import org.javaleo.grandpa.ejb.entities.price.Product;
import org.javaleo.grandpa.ejb.entities.price.Supplier;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Named
@Stateless
public class PriceFacade implements IPriceFacade {

	@Inject
	private IBrandBusiness brandBusiness;

	@Inject
	private ICategoryInputBusiness categoryInputBusiness;

	@Inject
	private IInputBusiness inputBusiness;

	@Inject
	private IMeasureBusiness measureBusiness;

	@Inject
	private IProductBusiness productBusiness;

	@Inject
	private ISupplierBusiness supplierBusiness;

	@Override
	public void saveBrand(Brand brand) throws BusinessException {
		brandBusiness.saveBrand(brand);
	}

	@Override
	public List<Brand> listAllBrands() {
		return brandBusiness.listAllBrands();
	}

	@Override
	public List<Brand> listAllActiveBrands() {
		return brandBusiness.listAllActiveBrands();
	}

	@Override
	public void deactivateBrand(Brand brand) throws BusinessException {
		brandBusiness.deactivateBrand(brand);
	}

	@Override
	public void saveCategoryInput(CategoryInput categoryInput) throws BusinessException {
		categoryInputBusiness.saveCategoryInput(categoryInput);
	}

	@Override
	public List<CategoryInput> listAllCategoryInputs() {
		return categoryInputBusiness.listAllCategoryInputs();
	}

	@Override
	public List<CategoryInput> listActiveCategoryInputs() {
		return categoryInputBusiness.listActiveCategoryInputs();
	}

	@Override
	public void deactivateCategoryInput(CategoryInput categoryInput) throws BusinessException {
		categoryInputBusiness.deactivateCategoryInput(categoryInput);
	}

	@Override
	public void saveInput(Input input) throws BusinessException {
		inputBusiness.saveInput(input);
	}

	@Override
	public List<Input> listAllInputs() {
		return inputBusiness.listAllInputs();
	}

	@Override
	public List<Input> listAllActiveInputs() {
		return inputBusiness.listAllActiveInputs();
	}

	@Override
	public void deactivateInput(Input input) throws BusinessException {
		inputBusiness.deactivateInput(input);
	}

	@Override
	public void saveMeasure(Measure measure) throws BusinessException {
		measureBusiness.saveMeasure(measure);
	}

	@Override
	public List<Measure> listAllMeasures() {
		return measureBusiness.listAllMeasures();
	}

	@Override
	public List<Measure> listAllActiveMeasures() {
		return measureBusiness.listAllActiveMeasures();
	}

	@Override
	public void deactivateMeasure(Measure measure) throws BusinessException {
		measureBusiness.deactivateMeasure(measure);
	}

	@Override
	public void saveProduct(Product product) throws BusinessException {
		productBusiness.saveProduct(product);
	}

	@Override
	public List<Product> listAllProducts() {
		return productBusiness.listAllProducts();
	}

	@Override
	public List<Product> listAllActiveProducts() {
		return productBusiness.listAllActiveProducts();
	}

	@Override
	public void deactivateProduct(Product product) throws BusinessException {
		productBusiness.deactivateProduct(product);
	}

	@Override
	public void saveSupplier(Supplier supplier) throws BusinessException {
		supplierBusiness.saveSupplier(supplier);
	}

	@Override
	public List<Supplier> listAllSuppliers() {
		return supplierBusiness.listAllSuppliers();
	}

	@Override
	public List<Supplier> listAllActiveSuppliers() {
		return supplierBusiness.listAllActiveSuppliers();
	}

	@Override
	public void deactivateSupplier(Supplier supplier) throws BusinessException {
		supplierBusiness.deactivateSupplier(supplier);
	}

}
