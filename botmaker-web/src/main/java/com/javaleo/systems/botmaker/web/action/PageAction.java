package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javaleo.systems.botmaker.ejb.entities.Page;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.filters.PageFilter;

@Named
@ConversationScoped
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
	private IBotMakerFacade facade;

	public String list() {
		startOrResumeConversation();
		if (filter == null) {
			filter = new PageFilter();
		}
		pageList = facade.listPages(filter);
		return PAGE_LIST;
	}

	public String startNew() {
		startOrResumeConversation();
		this.page = new Page();
		return PAGE_EDIT;
	}

	public String detail(Page page) {
		startOrResumeConversation();
		this.page = page;
		return PAGE_DETAIL;
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