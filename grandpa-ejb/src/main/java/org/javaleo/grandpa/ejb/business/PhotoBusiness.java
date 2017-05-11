package org.javaleo.grandpa.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Gallery;
import org.javaleo.grandpa.ejb.entities.Photo;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.PhotoFilter;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class PhotoBusiness implements IPhotoBusiness {

	private static final long serialVersionUID = 6533006211517890662L;

	@Inject
	private IPersistenceBasic<Photo> persistence;

	@Inject
	private IGalleryBusiness galleryBusiness;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean validatePhoto(Photo photo) throws BusinessException {
		return (StringUtils.isNotBlank(photo.getName()) && StringUtils.isNotBlank(photo.getUuid()));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePhoto(Photo photo) throws BusinessException {
		validatePhoto(photo);
		Gallery gallery = galleryBusiness.getGalleryByUuid(photo.getGallery().getUuid());
		photo.setGallery(gallery);
		persistence.saveOrUpdate(photo);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletePhoto(Photo photo) throws BusinessException {
		// TODO Auto-generated method stub
		persistence.remove(photo);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Photo> searchPhotos(PhotoFilter photoFilter) {
		// TODO Auto-generated method stub
		return null;
	}

}
