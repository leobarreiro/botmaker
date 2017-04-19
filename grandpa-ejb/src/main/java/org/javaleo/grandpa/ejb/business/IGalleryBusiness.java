package org.javaleo.grandpa.ejb.business;

import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Gallery;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.GalleryFilter;

@Local
public interface IGalleryBusiness {

	Gallery createGallery(String name, User user) throws BusinessException;

	void saveGallery(Gallery gallery) throws BusinessException;

	void deleteGallery(Gallery gallery) throws BusinessException;

	List<Gallery> searchGalleries(GalleryFilter galleryFilter);

}
