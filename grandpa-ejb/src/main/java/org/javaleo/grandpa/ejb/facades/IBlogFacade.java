package org.javaleo.grandpa.ejb.facades;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.blog.Blog;
import org.javaleo.grandpa.ejb.entities.blog.Category;
import org.javaleo.grandpa.ejb.entities.blog.Page;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IBlogFacade extends Serializable {

	List<Blog> listBlogs();

	Blog getBlogFromKey(String key);

	Blog getBlogFromId(Long id);

	List<Category> listAllCategoriesFromBlog(Blog blog);

	Category getCategoryFromKey(String key);

	Category getFirstCategoryOptionFromBlog(Blog blog);

	List<Page> listPagesFromBlogIdAndCategoryId(Long idBlog, Long idCategory) throws BusinessException;

	List<Page> listPagesFromBlogKeyAndCategoryKey(String blogKey, String categoryKey) throws BusinessException;

	List<Category> listActiveCategoriesFromBlog(Blog blog);

	Category getFirstCategoryOptionfromBlog(Blog blog);

}
