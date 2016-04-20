package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.filters.ValidatorFilter;
import com.javaleo.systems.botmaker.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class ValidatorAction extends AbstractCrudAction<Validator> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private MsgAction msgAction;

	private CRUD crudOp;

	private ValidatorFilter filter;
	private Validator validator;
	private List<Validator> validators;
	private List<ScriptType> scriptTypeOptions;

	@Inject
	private IBotMakerFacade facade;

	public String startNew() {
		startNewConversation();
		loadOptions();
		validator = new Validator();
		validator.setScriptType(ScriptType.REGEXP);
		return "/pages/validators/validator.jsf?faces-redirect=true";
	}

	public String save() {
		try {
			facade.saveValidator(validator);
			msgAction.addMessage(MessageType.INFO, "Validator saved.");
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return list();
	}

	public String edit() {
		startOrResumeConversation();
		loadOptions();
		return "/pages/validators/validator.jsf?faces-redirect=true";
	}

	public String detail(Validator validator) {
		startOrResumeConversation();
		this.validator = validator;
		return "/pages/validators/validator-detail.jsf?faces-redirect=true";
	}

	public String cancel() {
		return list();
	}

	public String list() {
		startOrResumeConversation();
		loadOptions();
		validators = facade.searchValidatorByFilter(filter);
		return "/pages/validators/validator-search.jsf?faces-redirect=true";
	}

	private void loadOptions() {
		filter = new ValidatorFilter();
		this.scriptTypeOptions = Arrays.asList(ScriptType.values());
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	@Override
	public CRUD getCrudOp() {
		return crudOp;
	}

	public void setCrudOp(CRUD crudOp) {
		this.crudOp = crudOp;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public List<Validator> getValidators() {
		return validators;
	}

	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}

	public List<ScriptType> getScriptTypeOptions() {
		return scriptTypeOptions;
	}

	public void setScriptTypeOptions(List<ScriptType> scriptTypeOptions) {
		this.scriptTypeOptions = scriptTypeOptions;
	}

	public ValidatorFilter getFilter() {
		return filter;
	}

	public void setFilter(ValidatorFilter filter) {
		this.filter = filter;
	}

}
