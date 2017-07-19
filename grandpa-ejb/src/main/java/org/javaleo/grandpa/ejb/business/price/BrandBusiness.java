package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.javaleo.grandpa.ejb.entities.price.Brand;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class BrandBusiness implements IBrandBusiness {

	@Inject
	private IPersistenceBasic<Brand> persistence;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveBrand(Brand brand) throws BusinessException {
		persistence.saveOrUpdate(brand);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Brand> listAllBrands() {
		Criteria crt = persistence.createCriteria(Brand.class, "br");
		crt.addOrder(Order.asc("br.name"));
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Brand> listAllActiveBrands() {
		Criteria crt = persistence.createCriteria(Brand.class, "br");
		crt.add(Restrictions.eq("br.active", Boolean.TRUE));
		crt.addOrder(Order.asc("br.name"));
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deactivateBrand(Brand brand) throws BusinessException {
		EntityManager em = persistence.getEntityManager();
		em.refresh(brand);
		brand.setActive(Boolean.FALSE);
		persistence.saveOrUpdate(brand);
	}

}
