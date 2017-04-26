package org.javaleo.grandpa.ejb.facades;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.business.IBlogBusiness;
import org.javaleo.grandpa.ejb.business.ICategoryBusiness;
import org.javaleo.grandpa.ejb.business.IPageBusiness;
import org.javaleo.grandpa.ejb.entities.Blog;
import org.javaleo.grandpa.ejb.entities.Category;
import org.javaleo.grandpa.ejb.entities.Page;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Named
@Stateless
public class BlogFacade implements IBlogFacade {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBlogBusiness blogBusiness;

	@Inject
	private ICategoryBusiness categoryBusiness;

	@Inject
	private IPageBusiness pageBusiness;

	public List<Blog> listBlogs() {
		return blogBusiness.listBlogs();
	}

	public Blog getBlogFromKey(String key) {
		return blogBusiness.getBlogFromKey(key);
	}

	public Category getFirstCategoryOptionFromBlog(Blog blog) {
		return categoryBusiness.getFirstCategoryOptionfromBlog(blog);
	}

	public List<Page> listPagesFromBlogIdAndCategoryId(Long idBlog, Long idCategory) throws BusinessException {
		return pageBusiness.listPagesFromBlogIdAndCategoryId(idBlog, idCategory);
	}

	public List<Page> listPagesFromBlogKeyAndCategoryKey(String blogKey, String categoryKey) throws BusinessException {
		return pageBusiness.listPagesFromBlogKeyAndCategoryKey(blogKey, categoryKey);
	}

}
