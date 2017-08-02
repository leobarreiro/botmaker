package org.javaleo.grandpa.web.action.price;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.price.Input;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IPriceFacade;
import org.javaleo.grandpa.web.action.AbstractCrudSinglePage;
import org.javaleo.grandpa.web.action.MsgAction;

@Named
@ConversationScoped
public class InputAction extends AbstractCrudSinglePage<Input> implements Serializable {

	private static final long serialVersionUID = -7000752518538111082L;

	private static final String THIS_PAGE = "/pages/price/inputs.bot?faces-redirect=true";

	@Inject
	private Conversation conversation;

	@Inject
	private IPriceFacade facade;

	@Inject
	private MsgAction msgAction;

	private Input input;
	private List<Input> inputList;

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
		this.input = new Input();
	}

	@Override
	public void searchAll() {
		inputList = facade.listAllInputs();
	}

	@Override
	public void searchOnlyActives() {
		inputList = facade.listAllActiveInputs();
	}

	@Override
	public void viewDetails(Input pojo) {
		this.input = pojo;
	}

	@Override
	public void startEdit(Input pojo) {
		this.input = pojo;
	}

	@Override
	public void deactivate(Input pojo) {
		this.input = pojo;
		try {
			facade.deactivateInput(input);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
	}

	@Override
	public void save() {
		try {
			facade.saveInput(input);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
	}

	// Getters and Setters

	public Input getInput() {
		return input;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	public List<Input> getInputList() {
		return inputList;
	}

	public void setInputList(List<Input> inputList) {
		this.inputList = inputList;
	}

}
