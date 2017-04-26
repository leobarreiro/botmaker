package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.Category;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IGrandPaFacade;
import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

@Named
@ConversationScoped
public class CategoryAction extends AbstractCrudAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CATEG_EDIT = "/pages/category/category.bot?faces-redirect=true";
	public static final String CATEG_DETAIL = "/pages/category/category-detail.bot?faces-redirect=true";
	public static final String CATEG_LIST = "/pages/category/category-search.bot?faces-redirect=true";

	private List<Category> categoryList;
	private Category category;

	@Inject
	private Conversation conversation;

	@Inject
	private MsgAction msgAction;

	@Inject
	private IGrandPaFacade facade;

	public String listCategories() {
		startOrResumeConversation();
		categoryList = facade.listAllCategories();
		return CATEG_LIST;
	}

	public String detailCategory(Category cat) {
		startOrResumeConversation();
		this.category = cat;
		return CATEG_DETAIL;
	}

	public String startNewCategory() {
		startOrResumeConversation();
		this.category = new Category();
		return CATEG_EDIT;
	}

	public String startEditCategory(Category cat) {
		startOrResumeConversation();
		this.category = cat;
		return CATEG_EDIT;
	}

	public String disableCategory() {
		startOrResumeConversation();
		facade.disableCategory(category);
		msgAction.addInfoMessage("Category disabled.");
		return CATEG_DETAIL;
	}

	public String enableCategory() {
		startOrResumeConversation();
		category.setActive(Boolean.TRUE);
		try {
			facade.saveCategory(category);
			msgAction.addInfoMessage("Category enabled.");
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		return CATEG_DETAIL;
	}

	public String saveCategory() {
		startOrResumeConversation();
		try {
			facade.saveCategory(category);
			msgAction.addInfoMessage("Category saved.");
			return CATEG_DETAIL;
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return CATEG_EDIT;
		}
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
