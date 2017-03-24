package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.enums.ValidatorType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.filters.ValidatorFilter;
import com.javaleo.systems.botmaker.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class ValidatorAction extends AbstractCrudAction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private UserPreferenceAction userPreferencesAction;

	@Inject
	private MsgAction msgAction;

	private ValidatorFilter filter;
	private Validator validator;
	private List<Validator> validators;
	private List<ScriptType> scriptTypeOptions;
	private List<ValidatorType> validatorTypeOptions;

	@Inject
	private IBotMakerFacade facade;

	public String startNew() {
		startNewConversation();
		loadOptions();
		validator = new Validator();
		validator.setValidatorType(ValidatorType.BOOLEAN);
		userPreferencesAction.loadPreferences();
		return "/pages/validators/validator.jsf?faces-redirect=true";
	}

	public String save() {
		try {
			facade.saveValidator(validator);
			msgAction.addMessage(MessageType.INFO, "Validator saved.");
			return detail(validator);
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return "/pages/validators/validator.jsf?faces-redirect=true";
		}
	}

	public String edit() {
		startOrResumeConversation();
		loadOptions();
		userPreferencesAction.loadPreferences();
		return "/pages/validators/validator.jsf?faces-redirect=true";
	}

	public String detail(Validator validator) {
		startOrResumeConversation();
		this.validator = validator;
		userPreferencesAction.loadPreferences();
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
		userPreferencesAction.loadPreferences();
		filter = new ValidatorFilter();
		this.scriptTypeOptions = Arrays.asList(ScriptType.values());
		this.validatorTypeOptions = new ArrayList<ValidatorType>(Arrays.asList(ValidatorType.values()));
	}

	@Override
	public Conversation getConversation() {
		return conversation;
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

	public List<ValidatorType> getValidatorTypeOptions() {
		return validatorTypeOptions;
	}

	public void setValidatorTypeOptions(List<ValidatorType> validatorTypeOptions) {
		this.validatorTypeOptions = validatorTypeOptions;
	}

	public ValidatorFilter getFilter() {
		return filter;
	}

	public void setFilter(ValidatorFilter filter) {
		this.filter = filter;
	}

}
