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
import javax.persistence.criteria.Root;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Blog;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.python.icu.util.Calendar;
import org.slf4j.Logger;

@Named
@Stateless
public class BlogBusiness implements IBlogBusiness {

	@Inject
	private IPersistenceBasic<Blog> persistence;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private Logger LOG;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Blog> listBlogs() {
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Blog> cq = cb.createQuery(Blog.class);
		Root<Blog> from = cq.from(Blog.class);
		Join<Blog, Company> joinComp = from.join("company");
		cq.where(cb.equal(joinComp.get("id"), company.getId()));
		cq.select(from);
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveBlog(Blog blog) throws BusinessException {
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		blog.setCompany(company);
		if (StringUtils.isBlank(blog.getName())) {
			throw new BusinessException("Blog name cant´t be empty.");
		}

		if (StringUtils.isBlank(blog.getKey())) {
			Calendar cal = Calendar.getInstance();
			StringBuilder sb = new StringBuilder();
			sb.append(blog.getCompany().getId());
			sb.append(blog.getName());
			sb.append(blog.getDescription());
			sb.append(cal.getTime());
			blog.setKey(DigestUtils.md5Hex(sb.toString().getBytes()));
		}
		persistence.saveOrUpdate(blog);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Blog getBlogFromKey(String key) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Blog> cq = cb.createQuery(Blog.class);
		Root<Blog> from = cq.from(Blog.class);
		cq.where(cb.equal(from.get("key"), key));
		cq.select(from);
		return persistence.getSingleResult(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Blog getBlogFromId(Long id) {
		return persistence.find(Blog.class, id);
	}
}
