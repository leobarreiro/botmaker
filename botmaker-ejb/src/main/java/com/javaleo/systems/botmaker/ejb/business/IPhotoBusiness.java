package com.javaleo.systems.botmaker.ejb.business;

import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Photo;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.PhotoFilter;

@Local
public interface IPhotoBusiness {

	void savePhoto(Photo photo) throws BusinessException;

	void deletePhoto(Photo photo) throws BusinessException;

	List<Photo> searchPhotos(PhotoFilter photoFilter);

}
