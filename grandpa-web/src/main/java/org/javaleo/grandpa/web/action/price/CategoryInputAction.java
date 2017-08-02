package org.javaleo.grandpa.web.action.price;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.price.CategoryInput;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IPriceFacade;
import org.javaleo.grandpa.web.action.AbstractCrudSinglePage;
import org.javaleo.grandpa.web.action.MsgAction;

@Named
@ConversationScoped
public class CategoryInputAction extends AbstractCrudSinglePage<CategoryInput> implements Serializable {

	private static final long serialVersionUID = -2269304675549519824L;

	private static final String THIS_PAGE = "/pages/price/category-inputs.bot?faces-redirect=true";

	@Inject
	private Conversation conversation;

	@Inject
	private IPriceFacade facade;

	@Inject
	private MsgAction msgAction;

	private CategoryInput categoryInput;
	private List<CategoryInput> categoryInputList;

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	@Override
	public String loadPage() {
		startConversation();
		startNew();
		searchOnlyActives();
		return THIS_PAGE;
	}

	@Override
	public void startNew() {
		this.categoryInput = new CategoryInput();
	}

	@Override
	public void searchAll() {
		categoryInputList = facade.listAllCategoryInputs();
	}

	@Override
	public void searchOnlyActives() {
		categoryInputList = facade.listActiveCategoryInputs();
	}

	@Override
	public void viewDetails(CategoryInput pojo) {
		this.categoryInput = pojo;
	}

	@Override
	public void startEdit(CategoryInput pojo) {
		this.categoryInput = pojo;
	}

	@Override
	public void deactivate(CategoryInput pojo) {
		this.categoryInput = pojo;
		try {
			facade.deactivateCategoryInput(categoryInput);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	@Override
	public void save() {
		try {
			facade.saveCategoryInput(categoryInput);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	// Getters and Setters

	public CategoryInput getCategoryInput() {
		return categoryInput;
	}

	public void setCategoryInput(CategoryInput categoryInput) {
		this.categoryInput = categoryInput;
	}

	public List<CategoryInput> getCategoryInputList() {
		return categoryInputList;
	}

	public void setCategoryInputList(List<CategoryInput> categoryInputList) {
		this.categoryInputList = categoryInputList;
	}

}
