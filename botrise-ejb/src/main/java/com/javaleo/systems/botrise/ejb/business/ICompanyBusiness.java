package com.javaleo.systems.botrise.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botrise.ejb.entities.Company;
import com.javaleo.systems.botrise.ejb.exceptions.BusinessException;

@Local
public interface ICompanyBusiness extends Serializable {

	List<Company> listAllCompanies();

	void saveCompany(Company company) throws BusinessException;

	void activateCompany(Company company) throws BusinessException;

	void deactivateCompany(Company company) throws BusinessException;

}
