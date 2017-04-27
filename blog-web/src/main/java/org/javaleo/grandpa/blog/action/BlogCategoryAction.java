package org.javaleo.grandpa.blog.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.Blog;
import org.javaleo.grandpa.ejb.entities.Category;
import org.javaleo.grandpa.ejb.facades.IBlogFacade;

@Named
@ConversationScoped
public class BlogCategoryAction extends AbstractBlogAction implements Serializable {

	private static final long serialVersionUID = -2392621123335818906L;

	@Inject
	private Conversation conversation;

	@Inject
	private IBlogFacade facade;

	private Category firstCategory;
	private List<Category> categories;

	public void initCategoriesFromBlog() {
		startOrResumeConversation();
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String blogKey = params.get("blog");
		Blog blog = facade.getBlogFromKey(blogKey);
		categories = facade.listActiveCategoriesFromBlog(blog);
		firstCategory = facade.getFirstCategoryOptionfromBlog(blog);
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Category getFirstCategory() {
		return firstCategory;
	}

	public void setFirstCategory(Category firstCategory) {
		this.firstCategory = firstCategory;
	}

}
