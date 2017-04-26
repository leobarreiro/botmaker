package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Blog;
import org.javaleo.grandpa.ejb.entities.Category;
import org.javaleo.grandpa.ejb.entities.Page;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.PageFilter;

@Local
public interface IPageBusiness extends Serializable {

	void savePage(Page page) throws BusinessException;

	List<Page> listPages(PageFilter pageFilter);

	List<Page> listLastPagesEdited();

	void disablePage(Page page) throws BusinessException;

	List<Page> listPagesFromBlog(Blog blog);

	List<Page> listPagesFromBlogIdAndCategoryId(Long idBlog, Long idCategory) throws BusinessException;

	List<Page> listPagesFromBlogKeyAndCategoryKey(String blogKey, String categoryKey) throws BusinessException;

	List<Page> listPagesFromCategory(Category category);

}
