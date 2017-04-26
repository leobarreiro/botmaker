package org.javaleo.grandpa.ejb.business;

import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Blog;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IBlogBusiness {

	List<Blog> listBlogs();

	void saveBlog(Blog blog) throws BusinessException;

	Blog getBlogFromKey(String key);

	Blog getBlogFromId(Long id);

}
