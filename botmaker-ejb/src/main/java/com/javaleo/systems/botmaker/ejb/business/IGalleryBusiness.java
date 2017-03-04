package com.javaleo.systems.botmaker.ejb.business;

import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Gallery;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.GalleryFilter;

@Local
public interface IGalleryBusiness {

	Gallery createGallery(String name, User user) throws BusinessException;

	void saveGallery(Gallery gallery) throws BusinessException;

	void deleteGallery(Gallery gallery) throws BusinessException;

	List<Gallery> searchGalleries(GalleryFilter galleryFilter);

}
