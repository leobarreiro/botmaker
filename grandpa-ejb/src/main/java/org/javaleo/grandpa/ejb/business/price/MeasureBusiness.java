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
import org.javaleo.grandpa.ejb.entities.price.Measure;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class MeasureBusiness implements IMeasureBusiness {

	@Inject
	private IPersistenceBasic<Measure> persistence;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveMeasure(Measure measure) throws BusinessException {
		persistence.saveOrUpdate(measure);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Measure> listAllMeasures() {
		Criteria crt = createCriteria();
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Measure> listAllActiveMeasures() {
		Criteria crt = createCriteria();
		crt.add(Restrictions.eq("ms.active", Boolean.TRUE));
		return persistence.getResultList(crt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deactivateMeasure(Measure measure) throws BusinessException {
		EntityManager em = persistence.getEntityManager();
		em.refresh(measure);
		measure.setActive(Boolean.FALSE);
		persistence.saveOrUpdate(measure);
	}

	private Criteria createCriteria() {
		Criteria crt = persistence.createCriteria(Measure.class, "ms");
		crt.addOrder(Order.asc("ms.name"));
		return crt;
	}

}
