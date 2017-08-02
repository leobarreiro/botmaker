package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.javaleo.grandpa.ejb.entities.price.CategoryInput;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class CategoryInputBusiness implements ICategoryInputBusiness {

	@Inject
	private IPersistenceBasic<CategoryInput> persistence;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveCategoryInput(CategoryInput categoryInput) throws BusinessException {
		persistence.saveOrUpdate(categoryInput);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<CategoryInput> listAllCategoryInputs() {
		Criteria crt = createCriteria();
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<CategoryInput> listActiveCategoryInputs() {
		Criteria crt = createCriteria();
		crt.add(Restrictions.eq("ci.active", Boolean.TRUE));
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deactivateCategoryInput(CategoryInput categoryInput) throws BusinessException {
		EntityManager em = persistence.getEntityManager();
		em.refresh(categoryInput);
		categoryInput.setActive(Boolean.FALSE);
		persistence.saveOrUpdate(categoryInput);
	}

	private Criteria createCriteria() {
		Criteria crt = persistence.createCriteria(CategoryInput.class, "ci");
		crt.addOrder(Order.asc("ci.name"));
		return crt;
	}

}
