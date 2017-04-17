package org.javaleo.grandpa.ejb.business;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Configuration;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IConfigurationBusiness {

	void saveConfiguration(Configuration configuration) throws BusinessException;

	Configuration getConfigurationByName(final String name);

	Integer getIntegerValueByConfiguration(Configuration configuration);

	String getStringValueByConfiguration(Configuration configuration);

	Float getFloatValueByConfiguration(Configuration configuration);

}
