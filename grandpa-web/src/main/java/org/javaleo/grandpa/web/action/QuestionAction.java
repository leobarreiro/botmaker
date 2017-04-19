package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Command;
import org.javaleo.grandpa.ejb.entities.Question;
import org.javaleo.grandpa.ejb.entities.Script;
import org.javaleo.grandpa.ejb.entities.Validator;
import org.javaleo.grandpa.ejb.enums.AnswerType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IBotMakerFacade;
import org.javaleo.grandpa.ejb.filters.ValidatorFilter;
import org.javaleo.grandpa.ejb.interceptors.EditingInterceptor;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.pojos.DialogContextVar;
import org.javaleo.grandpa.ejb.utils.GroovyScriptRunnerUtils;
import org.javaleo.grandpa.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
@Interceptors(EditingInterceptor.class)
public class QuestionAction extends AbstractConversationAction implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String PAGE_QUESTION = "/pages/question/question.bot?faces-redirect=true";
	private static final String PAGE_QUESTION_DETAIL = "/pages/question/question-detail.bot?faces-redirect=true";

	@Inject
	private Conversation conversation;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private GroovyScriptRunnerUtils groovyScriptRunner;

	@Inject
	private UserPreferenceAction userPreferencesAction;

	@Inject
	private MsgAction msgAction;

	@Inject
	private CommandAction commandAction;

	private Command command;
	private Question question;
	private List<Validator> validators;
	private ValidatorFilter validatorFilter;
	private List<DialogContextVar> contextVars;
	private String debugContent;
	private boolean debugging;

	public String startNew(Command command) {
		startOrResumeConversation();
		this.command = command;
		this.question = new Question();
		this.question.setPostScript(new Script());
		loadOptions();
		userPreferencesAction.loadPreferences();
		return PAGE_QUESTION;
	}

	public String edit(Question question) {
		startOrResumeConversation();
		this.question = question;
		if (this.question.getPostScript() == null) {
			this.question.setPostScript(new Script());
		}
		this.command = question.getCommand();
		loadOptions();
		userPreferencesAction.loadPreferences();
		return PAGE_QUESTION;
	}

	public String detail(Question question) {
		startOrResumeConversation();
		loadOptions();
		this.question = question;
		if (this.question.getPostScript() == null) {
			this.question.setPostScript(new Script());
		}
		this.command = question.getCommand();
		userPreferencesAction.loadPreferences();
		return PAGE_QUESTION_DETAIL;
	}

	public String dropQuestion() {
		try {
			facade.dropQuestion(question);
			msgAction.addInfoMessage("Question droped.");
			return commandAction.detail(command);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return PAGE_QUESTION_DETAIL;
		}
	}

	public String save() {
		try {
			question.setCommand(this.command);
			facade.saveQuestion(question);
			debugMode(false);
			msgAction.addMessage(MessageType.INFO, "Question saved");
			return detail(question);
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return PAGE_QUESTION;
		}
	}

	public void testScript() {
		try {
			Map<String, String> mapVars = new HashMap<String, String>();
			for (DialogContextVar ctx : contextVars) {
				if (StringUtils.isNotEmpty(ctx.getValue())) {
					mapVars.put(ctx.getName(), ctx.getValue());
				}
			}
			Dialog dialog = new Dialog();
			dialog.setBotId(question.getCommand().getBot().getId());
			dialog.setId(0);
			debugContent = (String) groovyScriptRunner.testScript(dialog, question.getPostScript().getCode());
		} catch (Exception e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
	}

	public void upQuestion(Question question) {
		try {
			command = question.getCommand();
			facade.upQuestionOrder(question);
			commandAction.setQuestions(facade.listQuestionsFromCommand(command));
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
	}

	public void answerTypeListener() {
		if (question.getAnswerType().equals(AnswerType.PHOTO) || question.getAnswerType().equals(AnswerType.DOCUMENT)) {
			question.setValidator(null);
		}
	}

	public void handleListValidators() {
		this.validators = facade.searchValidatorByFilter(validatorFilter);
	}

	public void selectValidator(Validator validator) {
		if (this.question != null) {
			this.question.setValidator(validator);
		}
	}

	public void loadOptions() {
		this.contextVars = facade.getListDialogContextVars();
		this.validatorFilter = new ValidatorFilter();
		// handleListValidators();
	}

	public void enablePostScript() {
		if (question.getProcessAnswer() && question.getPostScript() == null) {
			question.setPostScript(new Script());
		}
	}

	public boolean getEnableValidator() {
		return ((question.getAnswerType() != null) && (question.getAnswerType().equals(AnswerType.STRING) || question.getAnswerType().equals(AnswerType.NUMERIC)));
	}

	public void debugMode(boolean mode) {
		this.debugging = mode;
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public List<Validator> getValidators() {
		return validators;
	}

	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}

	public ValidatorFilter getValidatorFilter() {
		return validatorFilter;
	}

	public void setValidatorFilter(ValidatorFilter validatorFilter) {
		this.validatorFilter = validatorFilter;
	}

	public List<DialogContextVar> getContextVars() {
		return contextVars;
	}

	public void setContextVars(List<DialogContextVar> contextVars) {
		this.contextVars = contextVars;
	}

	public boolean isDebugging() {
		return debugging;
	}

	public void setDebugging(boolean debugging) {
		this.debugging = debugging;
	}

	public String getDebugContent() {
		return debugContent;
	}

	public void setDebugContent(String debugContent) {
		this.debugContent = debugContent;
	}

}
