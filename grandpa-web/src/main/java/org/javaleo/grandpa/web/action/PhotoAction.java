package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.Photo;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IGrandPaFacade;
import org.javaleo.grandpa.ejb.filters.PhotoFilter;
import org.slf4j.Logger;

@Named
@ConversationScoped
public class PhotoAction extends AbstractConversationAction implements Serializable {

	private static final long serialVersionUID = -1138770427899805745L;

	private static final String PAGE_PHOTO_1 = "/pages/photos/photo-1.bot?faces-redirect=true";
	private static final String PAGE_PHOTO_2 = "/pages/photos/photo-2.bot?faces-redirect=true";
	private static final String PAGE_DETAIL = "/pages/photos/pgoto-detail.bot?faces-redirect=true";
	private static final String PAGE_LIST = "/pages/photos/photo-search.bot?faces-redirect=true";

	private Photo photo;
	private List<Photo> photoList;
	private PhotoFilter filter;

	@Inject
	private Conversation conversation;

	@Inject
	private MsgAction msgAction;

	@Inject
	private Logger LOG;

	@Inject
	private IGrandPaFacade facade;

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public String startNewPhoto() {
		startNewConversation();
		photo = new Photo();
		return PAGE_PHOTO_1;
	}

	public String continueNewPhoto() {
		return PAGE_PHOTO_2;
	}

	public String startEdit(Photo pht) {
		startOrResumeConversation();
		photo = pht;
		return PAGE_PHOTO_2;
	}

	public String listPhotos() {
		startOrResumeConversation();
		if (filter == null) {
			filter = new PhotoFilter();
		}
		photoList = facade.searchPhotos(filter);
		return PAGE_LIST;
	}

	public String savePhoto() {
		startOrResumeConversation();
		try {
			facade.savePhoto(photo);
			msgAction.addInfoMessage("The Photo was save.");
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			LOG.warn(e.getMessage());
		}
		return listPhotos();
	}

	public String viewDetail(Photo pht) {
		startOrResumeConversation();
		photo = pht;
		return PAGE_DETAIL;
	}

	public String deletePhoto(Photo pht) {
		startOrResumeConversation();
		try {
			facade.deletePhoto(pht);
			;
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			LOG.warn(e.getMessage());
		}
		return listPhotos();
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public List<Photo> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<Photo> photoList) {
		this.photoList = photoList;
	}

	public PhotoFilter getFilter() {
		return filter;
	}

	public void setFilter(PhotoFilter filter) {
		this.filter = filter;
	}

}
