package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.javaleo.grandpa.ejb.entities.price.Product;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class ProductBusiness implements IProductBusiness {

	@Inject
	private IPersistenceBasic<Product> persistence;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveProduct(Product product) throws BusinessException {
		persistence.saveOrUpdate(product);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Product> listAllProducts() {
		Criteria crt = createCriteria();
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Product> listAllActiveProducts() {
		Criteria crt = createCriteria();
		crt.add(Restrictions.eq("pr.active", Boolean.TRUE));
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deactivateProduct(Product product) throws BusinessException {
		persistence.getEntityManager().refresh(product);
		product.setActive(Boolean.FALSE);
		persistence.saveOrUpdate(product);
	}

	private Criteria createCriteria() {
		Criteria crt = persistence.createCriteria(Product.class, "pr");
		crt.addOrder(Order.asc("pr.name"));
		return crt;
	}

}
