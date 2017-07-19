package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.blog.Gallery;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IGrandPaFacade;
import org.javaleo.grandpa.ejb.filters.GalleryFilter;
import org.slf4j.Logger;

@Named
@ConversationScoped
public class GalleryAction extends AbstractConversationAction implements Serializable {

	private static final long serialVersionUID = -1138770427899805745L;

	private static final String PAGE_GALLERY_1 = "/pages/galleries/gallery-1.bot?faces-redirect=true";
	private static final String PAGE_GALLERY_2 = "/pages/galleries/gallery-2.bot?faces-redirect=true";
	private static final String PAGE_DETAIL = "/pages/galleries/gallery-detail.bot?faces-redirect=true";
	private static final String PAGE_LIST = "/pages/galleries/gallery-search.bot?faces-redirect=true";

	private Gallery gallery;
	private List<Gallery> galleries;
	private GalleryFilter filter;

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

	public String startNewGallery() {
		startNewConversation();
		gallery = new Gallery();
		return PAGE_GALLERY_1;
	}

	public String continueNewGallery() {
		try {
			gallery = facade.createGallery(gallery.getName());
			return PAGE_GALLERY_2;
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			LOG.warn(e.getMessage());
			return PAGE_GALLERY_1;
		}
	}

	public String startEdit(Gallery gal) {
		startOrResumeConversation();
		gallery = gal;
		return PAGE_GALLERY_2;
	}

	public String listGalleries() {
		startOrResumeConversation();
		if (filter == null) {
			filter = new GalleryFilter();
		}
		galleries = facade.searchGalleries(filter);
		return PAGE_LIST;
	}

	public String saveGallery() {
		startOrResumeConversation();
		try {
			facade.saveGallery(gallery);
			msgAction.addInfoMessage("Gallery saved.");
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			LOG.warn(e.getMessage());
		}
		return listGalleries();
	}

	public String viewDetail(Gallery gal) {
		startOrResumeConversation();
		gallery = gal;
		return PAGE_DETAIL;
	}

	public String deleteGallery(Gallery gal) {
		startOrResumeConversation();
		try {
			facade.deleteGallery(gal);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			LOG.warn(e.getMessage());
		}
		return listGalleries();
	}

	public Gallery getGallery() {
		return gallery;
	}

	public void setGallery(Gallery gallery) {
		this.gallery = gallery;
	}

	public List<Gallery> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<Gallery> galleries) {
		this.galleries = galleries;
	}

	public GalleryFilter getFilter() {
		return filter;
	}

	public void setFilter(GalleryFilter filter) {
		this.filter = filter;
	}

}
