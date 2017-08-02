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
import org.javaleo.grandpa.ejb.entities.price.Input;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class InputBusiness implements IInputBusiness {

	@Inject
	private IPersistenceBasic<Input> persistence;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveInput(Input input) throws BusinessException {
		persistence.saveOrUpdate(input);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Input> listAllInputs() {
		Criteria crt = createCriteria();
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Input> listAllActiveInputs() {
		Criteria crt = createCriteria();
		crt.add(Restrictions.eq("it.active", Boolean.TRUE));
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deactivateInput(Input input) throws BusinessException {
		persistence.getEntityManager().refresh(input);
		input.setActive(Boolean.FALSE);
		persistence.saveOrUpdate(input);
	}

	private Criteria createCriteria() {
		Criteria crt = persistence.createCriteria(Input.class, "it");
		crt.addOrder(Order.asc("it.name"));
		return crt;
	}

}
