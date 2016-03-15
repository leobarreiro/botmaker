package com.javaleo.systems.botrise.web.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botrise.ejb.entities.Command;
import com.javaleo.systems.botrise.ejb.entities.Question;
import com.javaleo.systems.botrise.ejb.enums.AnswerType;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;
import com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade;
import com.javaleo.systems.botrise.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class QuestionAction extends AbstractCrudAction<Question> {

	private static final long serialVersionUID = 1L;

	private CRUD crud;

	@Inject
	private Conversation conversation;

	@Inject
	private IBotRiseFacade facade;

	@Inject
	private MsgAction msgAction;

	@Inject
	private CommandAction commandAction;

	private Command command;
	private Question question;
	private List<AnswerType> answerTypeOptions;

	public String startNew(Command command) {
		startOrResumeConversation();
		this.command = command;
		this.question = new Question();
		loadOptions();
		return "/pages/question/question.jsf?faces-redirect=true";
	}

	public String edit(Question question) {
		startOrResumeConversation();
		this.question = question;
		this.command = question.getCommand();
		loadOptions();
		return "/pages/question/question.jsf?faces-redirect=true";
	}

	public String detail(Question question) {
		startOrResumeConversation();
		this.question = question;
		this.command = question.getCommand();
		return "/pages/question/question-detail.jsf?faces-redirect=true";
	}

	public String save() {
		try {
			question.setCommand(this.command);
			facade.saveQuestion(question);
			msgAction.addMessage(MessageType.INFO, "Registro salvo corretamente");
			return commandAction.detail(this.command);
		} catch (BotRiseException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return "/pages/question/question.jsf?faces-redirect=true";
		}
	}

	private void loadOptions() {
		this.answerTypeOptions = new ArrayList<AnswerType>(Arrays.asList(AnswerType.values()));
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

	public List<AnswerType> getAnswerTypeOptions() {
		return answerTypeOptions;
	}

	public void setAnswerTypeOptions(List<AnswerType> answerTypeOptions) {
		this.answerTypeOptions = answerTypeOptions;
	}

}
