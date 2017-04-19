package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface ICompanyBusiness extends Serializable {

	List<Company> listAllCompanies();
	
	Company getCompanyById(Long id);

	void saveCompany(Company company) throws BusinessException;

	void activateCompany(Company company) throws BusinessException;

	void deactivateCompany(Company company) throws BusinessException;

}
