package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Blog;
import org.javaleo.grandpa.ejb.entities.Category;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface ICategoryBusiness extends Serializable {

	List<Category> listAllCategories();

	List<Category> listActiveCategories();

	Category getFirstCategoryOptionfromBlog(Blog blog);

	void saveCategory(Category category) throws BusinessException;

	void disableCategory(Category category);

}
