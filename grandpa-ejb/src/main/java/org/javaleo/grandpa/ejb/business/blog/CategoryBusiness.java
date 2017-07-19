package org.javaleo.grandpa.ejb.business.blog;

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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.business.ICompanyBusiness;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.blog.Blog;
import org.javaleo.grandpa.ejb.entities.blog.Category;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.python.icu.util.Calendar;
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
		cq.select(fromCat);
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Category> listAllCategoriesFromBlog(Blog blog) {
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Category> cq = cb.createQuery(Category.class);
		Root<Category> fromCat = cq.from(Category.class);
		Join<Category, Company> joinComp = fromCat.join("company", JoinType.INNER);
		Join<Category, Blog> joinBlog = fromCat.join("blog", JoinType.INNER);
		cq.where(cb.equal(joinComp.get("id"), company.getId()), cb.equal(joinBlog.get("id"), blog.getId()));
		cq.orderBy(cb.asc(fromCat.get("name")));
		cq.select(fromCat);
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Category> listActiveCategoriesFromBlog(Blog blog) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Category> cq = cb.createQuery(Category.class);
		Root<Category> fromCat = cq.from(Category.class);
		Join<Category, Blog> joinBlog = fromCat.join("blog", JoinType.INNER);
		cq.where(cb.equal(joinBlog.get("id"), blog.getId()), cb.equal(fromCat.get("active"), Boolean.TRUE));
		cq.orderBy(cb.desc(fromCat.get("firstOption")), cb.asc(fromCat.get("name")));
		cq.select(fromCat);
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Category getCategoryFromKey(String key) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Category> cq = cb.createQuery(Category.class);
		Root<Category> fromCat = cq.from(Category.class);
		Join<Category, Blog> joinBlog = fromCat.join("blog", JoinType.INNER);
		cq.where(cb.equal(fromCat.get("key"), key));
		cq.select(fromCat);
		return persistence.getSingleResult(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Category getFirstCategoryOptionFromBlog(Blog blog) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Category> cq = cb.createQuery(Category.class);
		Root<Category> fromCat = cq.from(Category.class);
		Join<Category, Blog> joinBlog = fromCat.join("blog", JoinType.INNER);
		cq.where(cb.equal(joinBlog.get("id"), blog.getId()), cb.equal(fromCat.get("firstOption"), Boolean.TRUE));
		cq.select(fromCat);
		return persistence.getSingleResult(cq);
	}

	@Override
	public void saveCategory(Category category) throws BusinessException {
		if (StringUtils.isBlank(category.getName())) {
			throw new BusinessException("Category Name can´t be empty.");
		}
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		category.setCompany(company);
		if (StringUtils.isBlank(category.getKey())) {
			Calendar cal = Calendar.getInstance();
			StringBuilder sb = new StringBuilder();
			sb.append(category.getCompany().getId());
			sb.append(category.getBlog().getId());
			sb.append(category.getName());
			sb.append(cal.getTime());
			category.setKey(DigestUtils.md5Hex(sb.toString().getBytes()));
		}
		persistence.saveOrUpdate(category);
	}

	@Override
	public void disableCategory(Category category) {
		category.setActive(Boolean.FALSE);
		persistence.saveOrUpdate(category);
	}

}
