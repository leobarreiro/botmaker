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
import org.javaleo.grandpa.ejb.entities.price.Supplier;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class SupplierBusiness implements ISupplierBusiness {

	@Inject
	private IPersistenceBasic<Supplier> persistence;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveSupplier(Supplier supplier) throws BusinessException {
		persistence.saveOrUpdate(supplier);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Supplier> listAllSuppliers() {
		Criteria crt = createCriteria();
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Supplier> listAllActiveSuppliers() {
		Criteria crt = createCriteria();
		crt.add(Restrictions.eq("sp.active", Boolean.TRUE));
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deactivateSupplier(Supplier supplier) throws BusinessException {
		persistence.getEntityManager().refresh(supplier);
		supplier.setActive(Boolean.FALSE);
		persistence.saveOrUpdate(supplier);
	}

	private Criteria createCriteria() {
		Criteria crt = persistence.createCriteria(Supplier.class, "sp");
		crt.addOrder(Order.asc("sp.name"));
		return crt;
	}

}
