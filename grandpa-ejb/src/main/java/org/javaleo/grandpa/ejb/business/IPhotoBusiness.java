package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Photo;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.PhotoFilter;

@Local
public interface IPhotoBusiness extends Serializable {

	boolean validatePhoto(Photo photo) throws BusinessException;

	void savePhoto(Photo photo) throws BusinessException;

	void deletePhoto(Photo photo) throws BusinessException;

	List<Photo> searchPhotos(PhotoFilter photoFilter);

}
