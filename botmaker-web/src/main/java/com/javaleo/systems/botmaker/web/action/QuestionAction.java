package com.javaleo.systems.botmaker.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.entities.Script;
import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.enums.AnswerType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.filters.ValidatorFilter;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.pojos.DialogContextVar;
import com.javaleo.systems.botmaker.ejb.utils.GroovyScriptRunnerUtils;
import com.javaleo.systems.botmaker.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class QuestionAction extends AbstractCrudAction<Question> {

	private static final String PAGE_QUESTION = "/pages/question/question.jsf?faces-redirect=true";
	private static final String PAGE_QUESTION_DETAIL = "/pages/question/question-detail.jsf?faces-redirect=true";

	private static final long serialVersionUID = 1L;

	private CRUD crud;

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
			debugContent = (String) groovyScriptRunner.testScript(dialog, question.getPostScript().getCode(), mapVars);
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

	private void loadOptions() {
		this.contextVars = facade.getListDialogContextVars();
		this.validators = facade.searchValidatorByFilter(new ValidatorFilter());
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

	@Override
	public CRUD getCrudOp() {
		return crud;
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
