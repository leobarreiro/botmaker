package org.javaleo.grandpa.ejb.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.Gallery;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.GalleryFilter;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.python.icu.util.Calendar;

@Stateless
public class GalleryBusiness implements IGalleryBusiness {

	private static final String GALLERIES = "galleries";

	private static final long serialVersionUID = -5321680667244713107L;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private IPersistenceBasic<Gallery> persistence;

	@Inject
	private IUserBusiness userBusiness;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Gallery getGalleryByUuid(String uuid) {
		CriteriaBuilder builder = persistence.getCriteriaBuilder();
		CriteriaQuery<Gallery> query = builder.createQuery(Gallery.class);
		Root<Gallery> from = query.from(Gallery.class);
		from.join("owner", JoinType.INNER);
		query.where(builder.equal(from.get("uuid"), uuid));
		query.select(from);
		return persistence.getSingleResult(query);
	}

	@Override
	public File getGalleryDir(Gallery gallery) throws IOException {
		StringBuilder pathDir = new StringBuilder(System.getProperty("botmaker.companies.dir"));
		pathDir.append(File.separator);
		pathDir.append(gallery.getCompany().getId());
		pathDir.append(File.separator);
		pathDir.append(GALLERIES);
		pathDir.append(File.separator);
		pathDir.append(gallery.getUuid());
		File galleryDir = new File(pathDir.toString());
		return galleryDir;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Gallery createGallery(String name) throws BusinessException {
		if (StringUtils.isBlank(name)) {
			throw new BusinessException("Gallery name can´t be empty.");
		}
		Gallery gallery = new Gallery();
		gallery.setName(StringUtils.trim(name));
		User user = userBusiness.findUserByUsername(credentials.getUser().getUsername());
		gallery.setOwner(user);
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		gallery.setCompany(company);
		StringBuilder str = new StringBuilder();
		str.append(name);
		str.append(company.getId());
		str.append(user.getEmail());
		String hash = DigestUtils.md5Hex(str.toString().getBytes());
		gallery.setUuid(hash);
		gallery.setCreated(Calendar.getInstance().getTime());
		gallery.setActive(Boolean.TRUE);
		persistence.saveOrUpdate(gallery);
		return gallery;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveGallery(Gallery gallery) throws BusinessException {
		try {
			File galleryDir = getGalleryDir(gallery);
			if (!galleryDir.exists()) {
				FileUtils.forceMkdir(galleryDir);
			}
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}
		gallery.setLastModified(Calendar.getInstance().getTime());
		persistence.saveOrUpdate(gallery);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteGallery(Gallery gallery) throws BusinessException {
		Gallery gall = persistence.find(Gallery.class, gallery.getId());
		try {
			File galleryDir = getGalleryDir(gall);
			FileUtils.forceDelete(galleryDir);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}
		persistence.remove(gall);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean validateGallery(Gallery gallery) throws BusinessException {
		return (StringUtils.isNotBlank(gallery.getName()));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Gallery> searchGalleries(GalleryFilter galleryFilter) {
		CriteriaBuilder builder = persistence.getCriteriaBuilder();
		CriteriaQuery<Gallery> query = builder.createQuery(Gallery.class);
		Root<Gallery> from = query.from(Gallery.class);
		Join<Gallery, User> joinUser = from.join("owner", JoinType.INNER);
		Join<Gallery, Company> joinComp = from.join("company", JoinType.INNER);
		List<Predicate> predicates = new ArrayList<Predicate>();
		Long companyId = credentials.getCompany().getId();
		predicates.add(builder.equal(joinComp.get("id"), companyId));

		if (StringUtils.isNotBlank(galleryFilter.getName())) {
			Expression<String> galleryName = from.get("name");
			predicates.add(builder.like(builder.lower(galleryName), StringUtils.lowerCase("%" + StringUtils.trim(galleryFilter.getName()) + "%")));
		}

		if (galleryFilter.getUser() != null && galleryFilter.getUser().getId() != null) {
			predicates.add(builder.equal(joinUser.get("id"), galleryFilter.getUser().getId()));
		}

		query.where(predicates.toArray(new Predicate[predicates.size()]));
		query.select(from);
		persistence.logQuery(query);
		return persistence.getResultList(query);
	}
}
