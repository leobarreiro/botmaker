package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.javaleo.grandpa.ejb.annotations.EditingNow;
import org.javaleo.grandpa.ejb.entities.Page;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IBotMakerFacade;
import org.javaleo.grandpa.ejb.filters.PageFilter;
import org.javaleo.grandpa.ejb.interceptors.EditingInterceptor;
import org.slf4j.Logger;

@Named
@ConversationScoped
@Interceptors(EditingInterceptor.class)
public class PageAction extends AbstractConversationAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PAGE_EDIT = "/pages/contents/page.jsf?faces-redirect=true";
	public static final String PAGE_DETAIL = "/pages/contents/page-detail.jsf?faces-redirect=true";
	public static final String PAGE_LIST = "/pages/contents/page-search.jsf?faces-redirect=true";

	private PageFilter filter;
	private List<Page> pageList;
	private Page page;

	@Inject
	private Conversation conversation;

	@Inject
	private Logger LOG;

	@Inject
	private MsgAction msgAction;

	@Inject
	private IBotMakerFacade facade;

	public String list() {
		startOrResumeConversation();
		if (filter == null) {
			filter = new PageFilter();
		}
		pageList = facade.listPages(filter);
		return PAGE_LIST;
	}

	@EditingNow(edit = true)
	public String startNew() {
		startOrResumeConversation();
		this.page = new Page();
		return PAGE_EDIT;
	}

	public String save() {
		startOrResumeConversation();
		try {
			facade.savePage(page);
			msgAction.addInfoMessage("Page saved!");
			return detail(page);
		} catch (BusinessException e) {
			LOG.warn(e.getMessage());
			msgAction.addErrorMessage(e.getMessage());
			return PAGE_EDIT;
		}
	}

	public void saveQuiet() {
		startOrResumeConversation();
		try {
			facade.savePage(page);
		} catch (BusinessException e) {
			LOG.warn(e.getMessage());
			msgAction.addErrorMessage(e.getMessage());
		}
	}

	public String detail(Page page) {
		startOrResumeConversation();
		this.page = page;
		return PAGE_DETAIL;
	}

	@EditingNow(edit = true)
	public String edit(Page page) {
		startOrResumeConversation();
		this.page = page;
		return PAGE_EDIT;
	}

	public String dropPage() {
		startOrResumeConversation();
		// TODO realizar drop da pagina
		return list();
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public PageFilter getFilter() {
		return filter;
	}

	public void setFilter(PageFilter filter) {
		this.filter = filter;
	}

	public List<Page> getPageList() {
		return pageList;
	}

	public void setPageList(List<Page> pageList) {
		this.pageList = pageList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
