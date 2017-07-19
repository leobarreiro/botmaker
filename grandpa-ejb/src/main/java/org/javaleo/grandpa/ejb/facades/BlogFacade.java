package org.javaleo.grandpa.ejb.facades;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.business.blog.IBlogBusiness;
import org.javaleo.grandpa.ejb.business.blog.ICategoryBusiness;
import org.javaleo.grandpa.ejb.business.blog.IPageBusiness;
import org.javaleo.grandpa.ejb.entities.blog.Blog;
import org.javaleo.grandpa.ejb.entities.blog.Category;
import org.javaleo.grandpa.ejb.entities.blog.Page;
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

	@Override
	public List<Blog> listBlogs() {
		return blogBusiness.listBlogs();
	}

	@Override
	public Blog getBlogFromKey(String key) {
		return blogBusiness.getBlogFromKey(key);
	}

	@Override
	public Blog getBlogFromId(Long id) {
		return blogBusiness.getBlogFromId(id);
	}

	@Override
	public List<Category> listAllCategoriesFromBlog(Blog blog) {
		return categoryBusiness.listAllCategoriesFromBlog(blog);
	}

	@Override
	public Category getCategoryFromKey(String key) {
		return categoryBusiness.getCategoryFromKey(key);
	}

	@Override
	public Category getFirstCategoryOptionFromBlog(Blog blog) {
		return categoryBusiness.getFirstCategoryOptionFromBlog(blog);
	}

	@Override
	public List<Page> listPagesFromBlogIdAndCategoryId(Long idBlog, Long idCategory) throws BusinessException {
		return pageBusiness.listPagesFromBlogIdAndCategoryId(idBlog, idCategory);
	}

	@Override
	public List<Page> listPagesFromBlogKeyAndCategoryKey(String blogKey, String categoryKey) throws BusinessException {
		return pageBusiness.listPagesFromBlogKeyAndCategoryKey(blogKey, categoryKey);
	}

	@Override
	public List<Category> listActiveCategoriesFromBlog(Blog blog) {
		return categoryBusiness.listActiveCategoriesFromBlog(blog);
	}

	@Override
	public Category getFirstCategoryOptionfromBlog(Blog blog) {
		return categoryBusiness.getFirstCategoryOptionFromBlog(blog);
	}

}
