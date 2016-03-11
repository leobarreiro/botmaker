package com.javaleo.systems.botrise.web.action;

import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botrise.ejb.entities.Command;
import com.javaleo.systems.botrise.ejb.entities.Question;
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

	private Command command;
	private Question question;
	private List<Question> questions;

	public String startNew() {
		startOrResumeConversation();
		return "/pages/question/question.jsf?faces-redirect=true";
	}

	public void search() {
		this.questions = facade.listQuestionsByCommand(command);
	}

	public String save() {
		try {
			facade.saveQuestion(question);
			msgAction.addMessage(MessageType.INFO, "Registro salvo corretamente");
			return "/pages/command/command-detail.jsf?faces-redirect=true";
		} catch (BotRiseException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return "/pages/question/question.jsf?faces-redirect=true";
		}
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

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

}
