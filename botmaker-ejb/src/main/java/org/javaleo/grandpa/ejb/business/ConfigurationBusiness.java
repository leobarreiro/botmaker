package org.javaleo.grandpa.ejb.business;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.Configuration;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class ConfigurationBusiness implements IConfigurationBusiness {

	@Inject
	private IPersistenceBasic<Configuration> persistence;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveConfiguration(Configuration configuration) throws BusinessException {
		if (StringUtils.isBlank(StringUtils.trim(configuration.getName()))) {
			throw new BusinessException("Configuration Name can´t be empty.");
		}
		if (configuration.getConfigurationType() == null) {
			throw new BusinessException("Configuration Type can´t be null.");
		}
		if (StringUtils.isBlank(StringUtils.trim(configuration.getValue()))) {
			throw new BusinessException("Configuration Value can´t be empty.");
		}
		configuration.setName(StringUtils.upperCase(configuration.getName()));
		configuration.setValue(StringUtils.trim(configuration.getValue()));
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		configuration.setCompany(company);
		persistence.saveOrUpdate(configuration);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Configuration getConfigurationByName(String name) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Configuration> cq = cb.createQuery(Configuration.class);
		Root<Configuration> from = cq.from(Configuration.class);
		Join<Configuration, Company> joinCompany = from.join("company", JoinType.INNER);
		Predicate whereName = cb.like(cb.upper(from.<String> get("name")), StringUtils.upperCase(StringUtils.trim(name)));
		Predicate whereCompany = cb.equal(joinCompany.get("id"), credentials.getCompany().getId());
		cq.where(cb.and(whereName, whereCompany));
		cq.select(from);
		return persistence.getSingleResult(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Integer getIntegerValueByConfiguration(Configuration configuration) {
		Integer value = Integer.valueOf(StringUtils.trim(configuration.getValue()));
		return value;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String getStringValueByConfiguration(Configuration configuration) {
		String value = StringUtils.trim(configuration.getValue());
		return value;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Float getFloatValueByConfiguration(Configuration configuration) {
		Float value = (Float) Float.valueOf(StringUtils.trim(configuration.getValue()));
		return value;
	}

}
