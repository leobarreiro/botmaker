package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.price.Measure;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class MeasureBusiness implements IMeasureBusiness {

	@Inject
	private IPersistenceBasic<Measure> persistence;
	
	@Override
	public void saveMeasure(Measure measure) throws BusinessException {
		persistence.saveOrUpdate(measure);
	}

	@Override
	public List<Measure> listAllMeasures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Measure> listAllActiveMeasures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deactivateMeasure(Measure measure) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	
	
}
