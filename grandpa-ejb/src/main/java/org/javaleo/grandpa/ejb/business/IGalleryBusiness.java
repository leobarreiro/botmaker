package org.javaleo.grandpa.ejb.business;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Gallery;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.GalleryFilter;

@Local
public interface IGalleryBusiness extends Serializable {

	Gallery getGalleryByUuid(String uuid);

	File getGalleryDir(Gallery gallery) throws IOException;

	Gallery createGallery(String name) throws BusinessException;

	void saveGallery(Gallery gallery) throws BusinessException;

	boolean validateGallery(Gallery gallery) throws BusinessException;

	void deleteGallery(Gallery gallery) throws BusinessException;

	List<Gallery> searchGalleries(GalleryFilter galleryFilter);

}
