package com.javaleo.systems.botmaker.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Named
@Stateless
public class CompanyBusiness implements ICompanyBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Company> persistence;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Company> listAllCompanies() {
		CriteriaQuery<Company> cq = persistence.createCriteriaQuery(Company.class);
		Root<Company> from = cq.from(Company.class);
		cq.select(from);
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Company getCompanyById(Long id) {
		return persistence.find(Company.class, id);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveCompany(Company company) throws BusinessException {
		if (StringUtils.isBlank(company.getName())) {
			throw new BusinessException("Name of Company don't be null or empty.");
		}
		persistence.saveOrUpdate(company);
		persistence.getEntityManager().flush();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void activateCompany(Company company) throws BusinessException {
		company.setActive(true);
		saveCompany(company);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deactivateCompany(Company company) throws BusinessException {
		company.setActive(false);
		saveCompany(company);
	}

}
