package org.javaleo.grandpa.ejb.business.price;

import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.price.Measure;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IMeasureBusiness {
	
	void saveMeasure(Measure measure) throws BusinessException;
	
	List<Measure> listAllMeasures();
	
	List<Measure> listAllActiveMeasures();
	
	void deactivateMeasure(Measure measure) throws BusinessException;

}
