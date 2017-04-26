package org.javaleo.grandpa.blog.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.Blog;
import org.javaleo.grandpa.ejb.entities.Category;
import org.javaleo.grandpa.ejb.entities.Page;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IBlogFacade;
import org.javaleo.grandpa.ejb.filters.PageFilter;

@Named
@ConversationScoped
public class BlogPagesAction extends AbstractBlogAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PAGE_DETAIL = "/page-detail.botf?faces-redirect=true";
	public static final String PAGE_LIST = "/pages/contents/page-search.bot?faces-redirect=true";

	private PageFilter filter;
	private List<Page> pageList;
	private Page page;

	@Inject
	private Conversation conversation;

	@Inject
	private IBlogFacade facade;

	public String viewDetail(Page page) {
		startOrResumeConversation();
		this.page = page;
		return PAGE_DETAIL;
	}

	public void listPages() {
		startOrResumeConversation();
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Blog blog;
		if (params.containsKey("blog")) {
			String blogKey = params.get("blog");
			blog = facade.getBlogFromKey(blogKey);
		} else {
			blog = facade.getBlogFromId(1L);
		}

		if (blog == null) {
			pageList = new ArrayList<Page>();
			return;
		}

		Category category;
		if (params.containsKey("cat")) {
			String categoryKey = params.get("cat");
			category = facade.getCategoryFromKey(categoryKey);
		} else {
			category = facade.getFirstCategoryOptionFromBlog(blog);
		}

		try {
			pageList = facade.listPagesFromBlogKeyAndCategoryKey(blog.getKey(), category.getKey());
		} catch (BusinessException e) {
			pageList = new ArrayList<Page>();
		}
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
