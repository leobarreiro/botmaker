package org.javaleo.grandpa.ejb.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Category;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.Page;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.PageFilter;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Stateless
public class PageBusiness implements IPageBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Page> persistence;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Inject
	private IUserBusiness userBusiness;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePage(Page page) throws BusinessException {
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		page.setCompany(company);
		User user = userBusiness.findUserByUsername(credentials.getUser().getUsername());
		page.setUser(user);
		if (page.getCreated() == null) {
			page.setCreated(Calendar.getInstance().getTime());
		}
		page.setEdited(Calendar.getInstance().getTime());
		persistence.saveOrUpdate(page);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Page> listPages(PageFilter pageFilter) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Page> cq = cb.createQuery(Page.class);
		Root<Page> fromPage = cq.from(Page.class);
		Join<Page, Company> joinCompany = fromPage.join("company", JoinType.INNER);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(joinCompany.get("id"), credentials.getCompany().getId()));
		if (StringUtils.isNotBlank(pageFilter.getTitle())) {
			Expression<String> path = fromPage.get("title");
			predicates.add(cb.like(path, "%" + pageFilter.getTitle() + "%"));
		}
		if (pageFilter.getActive() != null) {
			predicates.add(cb.equal(fromPage.get("active"), pageFilter.getActive()));
		}
		if (StringUtils.isNotBlank(pageFilter.getContent())) {
			Expression<String> pathContent = fromPage.get("content");
			predicates.add(cb.like(pathContent, "%" + pageFilter.getContent() + "%"));
		}
		cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		cq.orderBy(cb.desc(fromPage.get("edited")));
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Page> listLastPagesEdited() {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Page> cq = cb.createQuery(Page.class);
		Root<Page> fromPage = cq.from(Page.class);
		Join<Page, Company> joinCompany = fromPage.join("company", JoinType.INNER);
		cq.where(cb.equal(joinCompany.get("id"), credentials.getCompany().getId()));
		cq.orderBy(cb.desc(fromPage.get("edited")));
		return persistence.getResultList(cq, 5);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void disablePage(Page page) throws BusinessException {
		Page managedPage = persistence.find(Page.class, page.getId());
		managedPage.setActive(false);
		persistence.saveOrUpdate(managedPage);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Page> listPagesFromBlog() {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Page> cq = cb.createQuery(Page.class);
		Root<Page> fromPage = cq.from(Page.class);
		cq.where(cb.equal(fromPage.get("published"), Boolean.TRUE));
		cq.orderBy(cb.desc(fromPage.get("created")));
		return persistence.getResultList(cq, 10);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Page> listPagesFromCategory(Category category) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Page> cq = cb.createQuery(Page.class);
		Root<Page> fromPage = cq.from(Page.class);
		Join<Page, Company> joinCompany = fromPage.join("company", JoinType.INNER);
		Join<Page, Category> joinCategory = fromPage.join("category", JoinType.INNER);
		cq.where(cb.equal(joinCompany.get("id"), credentials.getCompany().getId()));
		Predicate whereCompany = cb.equal(joinCompany.get("id"), credentials.getCompany().getId());
		Predicate whereCategory = cb.equal(joinCategory.get("id"), category.getId());
		cq.where(whereCompany, whereCategory);
		cq.orderBy(cb.desc(fromPage.get("created")));
		return persistence.getResultList(cq);
	}

}
