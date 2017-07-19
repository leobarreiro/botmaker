package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.blog.Blog;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IGrandPaFacade;
import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

@Named
@ConversationScoped
public class BlogAction extends AbstractCrudAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String BLOG_EDIT = "/pages/blog/blog.bot?faces-redirect=true";
	public static final String BLOG_DETAIL = "/pages/blog/blog-detail.bot?faces-redirect=true";
	public static final String BLOG_LIST = "/pages/blog/blog-search.bot?faces-redirect=true";

	private List<Blog> blogs;
	private Blog blog;

	@Inject
	private Conversation conversation;

	@Inject
	private MsgAction msgAction;

	@Inject
	private IGrandPaFacade facade;

	public String listBlogs() {
		startOrResumeConversation();
		blogs = facade.listBlogs();
		return BLOG_LIST;
	}

	public String detailBlog(Blog blog) {
		startOrResumeConversation();
		this.blog = blog;
		return BLOG_DETAIL;
	}

	public String startNewBlog() {
		startOrResumeConversation();
		this.blog = new Blog();
		return BLOG_EDIT;
	}

	public String startEditBlog(Blog blog) {
		startOrResumeConversation();
		this.blog = blog;
		return BLOG_EDIT;
	}

	public String saveBlog() {
		try {
			facade.saveBlog(blog);
			msgAction.addInfoMessage("Blog saved.");
			return BLOG_DETAIL;
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return BLOG_EDIT;
		}
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

}
