package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.Validator;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.enums.ValidatorType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IBotMakerFacade;
import org.javaleo.grandpa.ejb.filters.ValidatorFilter;
import org.javaleo.grandpa.web.action.MsgAction.MessageType;
import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

@Named
@ConversationScoped
public class ValidatorAction extends AbstractCrudAction implements Serializable {

	private static final String PAGE_LIST = "/pages/validators/validator-search.bot?faces-redirect=true";

	private static final String PAGE_DETAIL = "/pages/validators/validator-detail.bot?faces-redirect=true";

	private static final String PAGE_EDIT = "/pages/validators/validator.bot?faces-redirect=true";

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private UserPreferenceAction userPreferencesAction;

	@Inject
	private MsgAction msgAction;

	private ValidatorFilter filter;
	private Validator validator;
	private Validator viewValidator;
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
		return PAGE_EDIT;
	}

	public String save() {
		try {
			facade.saveValidator(validator);
			msgAction.addMessage(MessageType.INFO, "Validator saved.");
			return detail(validator);
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return PAGE_EDIT;
		}
	}

	public String startEditValidator(Validator validator) {
		startOrResumeConversation();
		this.validator = validator;
		loadOptions();
		return PAGE_EDIT;
	}

	public void startViewValidator(Validator viewValidator) {
		startOrResumeConversation();
		this.viewValidator = viewValidator;
		loadOptions();
	}

	public String detail(Validator validator) {
		startOrResumeConversation();
		this.validator = validator;
		userPreferencesAction.loadPreferences();
		return PAGE_DETAIL;
	}

	public String cancel() {
		return list();
	}

	public String list() {
		startOrResumeConversation();
		loadOptions();
		validators = facade.searchValidatorByFilter(filter);
		return PAGE_LIST;
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

	public Validator getViewValidator() {
		return viewValidator;
	}

	public void setViewValidator(Validator viewValidator) {
		this.viewValidator = viewValidator;
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
