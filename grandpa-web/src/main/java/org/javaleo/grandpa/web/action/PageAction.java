package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.javaleo.grandpa.ejb.annotations.EditingNow;
import org.javaleo.grandpa.ejb.entities.blog.Blog;
import org.javaleo.grandpa.ejb.entities.blog.Category;
import org.javaleo.grandpa.ejb.entities.blog.Page;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IGrandPaFacade;
import org.javaleo.grandpa.ejb.filters.PageFilter;
import org.javaleo.grandpa.ejb.interceptors.EditingInterceptor;
import org.slf4j.Logger;

@Named
@ConversationScoped
@Interceptors(EditingInterceptor.class)
public class PageAction extends AbstractConversationAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PAGE_EDIT = "/pages/contents/page.bot?faces-redirect=true";
	public static final String PAGE_DETAIL = "/pages/contents/page-detail.bot?faces-redirect=true";
	public static final String PAGE_LIST = "/pages/contents/page-search.bot?faces-redirect=true";

	private Blog blog;
	private List<Blog> blogList;
	private PageFilter filter;
	private List<Page> pageList;
	private Page page;
	private List<Category> categories;

	@Inject
	private Conversation conversation;

	@Inject
	private Logger LOG;

	@Inject
	private MsgAction msgAction;

	@Inject
	private IGrandPaFacade facade;

	public String list() {
		startOrResumeConversation();
		initLoad();
		pageList = facade.listPages(filter);
		return PAGE_LIST;
	}

	@EditingNow(edit = true)
	public String startNew() {
		startOrResumeConversation();
		initLoad();
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
		initLoad();
		return PAGE_EDIT;
	}

	public String dropPage() {
		startOrResumeConversation();
		// TODO realizar drop da pagina
		return list();
	}

	public void handleCategories() {
		filter.setCategoryOpt(facade.listActiveCategoriesFromBlog(filter.getBlog()));
	}

	private void initLoad() {
		if (filter == null) {
			filter = new PageFilter();
		}
		blogList = facade.listBlogs();
		filter.setBlogOpt(facade.listBlogs());
		categories = facade.listAllCategories();
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public List<Blog> getBlogList() {
		return blogList;
	}

	public void setBlogList(List<Blog> blogList) {
		this.blogList = blogList;
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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

}
