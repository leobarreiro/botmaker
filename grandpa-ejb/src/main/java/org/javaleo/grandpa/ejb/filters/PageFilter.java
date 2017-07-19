package org.javaleo.grandpa.ejb.filters;

import java.util.List;

import org.javaleo.grandpa.ejb.entities.blog.Blog;
import org.javaleo.grandpa.ejb.entities.blog.Category;

public class PageFilter {

	private Blog blog;
	private Category category;
	private List<Blog> blogOpt;
	private List<Category> categoryOpt;
	private String title;
	private String content;
	private Boolean active;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Blog> getBlogOpt() {
		return blogOpt;
	}

	public void setBlogOpt(List<Blog> blogOpt) {
		this.blogOpt = blogOpt;
	}

	public List<Category> getCategoryOpt() {
		return categoryOpt;
	}

	public void setCategoryOpt(List<Category> categoryOpt) {
		this.categoryOpt = categoryOpt;
	}

}
