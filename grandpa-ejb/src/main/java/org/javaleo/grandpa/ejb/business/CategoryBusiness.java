package org.javaleo.grandpa.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Category;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.slf4j.Logger;

@Named
@Stateless
public class CategoryBusiness implements ICategoryBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Category> persistence;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Inject
	private Logger LOG;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Category> listAllCategories() {
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Category> cq = cb.createQuery(Category.class);
		Root<Category> fromCat = cq.from(Category.class);
		Join<Category, Company> joinComp = fromCat.join("company", JoinType.INNER);
		cq.where(cb.equal(joinComp.get("id"), company.getId()));
		cq.orderBy(cb.asc(fromCat.get("name")));
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Category> listActiveCategories() {
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Category> cq = cb.createQuery(Category.class);
		Root<Category> fromCat = cq.from(Category.class);
		Join<Category, Company> joinComp = fromCat.join("company", JoinType.INNER);
		cq.where(cb.equal(joinComp.get("id"), company.getId()), cb.equal(fromCat.get("active"), Boolean.TRUE));
		cq.orderBy(cb.asc(fromCat.get("name")));
		return persistence.getResultList(cq);
	}

	@Override
	public void saveCategory(Category category) throws BusinessException {
		if (StringUtils.isBlank(category.getName())) {
			throw new BusinessException("Category Name can´t be empty.");
		}
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		category.setCompany(company);
		persistence.saveOrUpdate(category);
	}

	@Override
	public void disableCategory(Category category) {
		category.setActive(Boolean.FALSE);
		persistence.saveOrUpdate(category);
	}

}
