package org.javaleo.grandpa.ejb.business.blog;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.blog.Photo;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.PhotoFilter;

@Local
public interface IPhotoBusiness extends Serializable {

	boolean validatePhoto(Photo photo) throws BusinessException;

	File getFileFromPhoto(Photo photo) throws BusinessException, IOException;

	File getFileThumbnailFromPhoto(Photo photo) throws BusinessException, IOException;

	void savePhoto(Photo photo) throws BusinessException;

	void deletePhoto(Photo photo) throws BusinessException;

	List<Photo> searchPhotos(PhotoFilter photoFilter);

}
