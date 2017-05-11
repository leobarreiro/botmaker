package org.javaleo.grandpa.ejb.business;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.javaleo.grandpa.ejb.entities.Gallery;
import org.javaleo.grandpa.ejb.entities.Photo;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.PhotoFilter;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Named
@Stateless
public class PhotoBusiness implements IPhotoBusiness {

	private static final String THUMBS = "thumbs";

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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public File getFileFromPhoto(Photo photo) throws BusinessException, IOException {
		File galleryDir = galleryBusiness.getGalleryDir(photo.getGallery());
		StringBuilder str = new StringBuilder();
		str.append(galleryDir.getCanonicalPath());
		str.append(File.separator);
		str.append(photo.getName());
		File photoFile = new File(str.toString());
		return photoFile;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public File getFileThumbnailFromPhoto(Photo photo) throws BusinessException, IOException {
		File galleryDir = galleryBusiness.getGalleryDir(photo.getGallery());
		StringBuilder str = new StringBuilder();
		str.append(galleryDir.getCanonicalPath());
		str.append(File.separator);
		str.append(THUMBS);
		str.append(File.separator);
		str.append(photo.getName());
		File photoFile = new File(str.toString());
		return photoFile;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePhoto(Photo photo) throws BusinessException {
		validatePhoto(photo);
		Gallery gallery = galleryBusiness.getGalleryByUuid(photo.getGallery().getUuid());
		photo.setGallery(gallery);
		// TODO: make a logical to compose a better hash, using company Id, gallery name and photo name
		photo.setUuid(DigestUtils.md5Hex(photo.getName()));

		try {
			// http://www.javaroots.com/2013/09/how-to-create-thumbnail-images-in-java.html
			File photoFile = getFileFromPhoto(photo);
			FileUtils.writeByteArrayToFile(photoFile, photo.getContent());
			BufferedImage buf = ImageIO.read(photoFile);
			BufferedImage thumb = Scalr.resize(buf, Method.QUALITY, Mode.FIT_TO_WIDTH, 120, 120, Scalr.OP_ANTIALIAS);
			File thumbFile = getFileThumbnailFromPhoto(photo);
			ImageIO.write(thumb, FilenameUtils.getExtension(thumbFile.getCanonicalPath()), thumbFile);
			persistence.saveOrUpdate(photo);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}

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
