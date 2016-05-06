package com.javaleo.systems.botmaker.web.action;

import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.filters.ValidatorFilter;
import com.javaleo.systems.botmaker.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class QuestionAction extends AbstractCrudAction<Question> {

	private static final long serialVersionUID = 1L;

	private CRUD crud;

	@Inject
	private Conversation conversation;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private UserPreferenceAction userPreferencesAction;

	@Inject
	private MsgAction msgAction;

	@Inject
	private CommandAction commandAction;

	private Command command;
	private Question question;
	private List<Validator> validators;

	public String startNew(Command command) {
		startOrResumeConversation();
		this.command = command;
		this.question = new Question();
		loadOptions();
		userPreferencesAction.loadPreferences();
		return "/pages/question/question.jsf?faces-redirect=true";
	}

	public String edit(Question question) {
		startOrResumeConversation();
		this.question = question;
		this.command = question.getCommand();
		loadOptions();
		userPreferencesAction.loadPreferences();
		return "/pages/question/question.jsf?faces-redirect=true";
	}

	public String detail(Question question) {
		startOrResumeConversation();
		this.question = question;
		this.command = question.getCommand();
		userPreferencesAction.loadPreferences();
		return "/pages/question/question-detail.jsf?faces-redirect=true";
	}

	public String save() {
		try {
			question.setCommand(this.command);
			facade.saveQuestion(question);
			msgAction.addMessage(MessageType.INFO, "Question saved");
			return commandAction.detail(this.command);
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return "/pages/question/question.jsf?faces-redirect=true";
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

	private void loadOptions() {
		this.validators = facade.searchValidatorByFilter(new ValidatorFilter());
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

}
