package org.javaleo.grandpa.ejb.business.blog;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.blog.Blog;
import org.javaleo.grandpa.ejb.entities.blog.Category;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface ICategoryBusiness extends Serializable {

	List<Category> listAllCategories();

	List<Category> listAllCategoriesFromBlog(Blog blog);

	List<Category> listActiveCategoriesFromBlog(Blog blog);

	Category getCategoryFromKey(final String key);

	Category getFirstCategoryOptionFromBlog(Blog blog);

	void saveCategory(Category category) throws BusinessException;

	void disableCategory(Category category);

}
