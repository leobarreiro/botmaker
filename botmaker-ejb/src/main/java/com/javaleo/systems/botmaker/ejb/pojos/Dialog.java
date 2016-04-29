package com.javaleo.systems.botmaker.ejb.pojos;

import java.io.Serializable;
import java.util.List;

import org.javaleo.libs.botgram.model.Message;
import org.javaleo.libs.botgram.model.Update;

import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;

public class Dialog implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idChat;
	private Update update;
	private Command command;
	private String postProcessedResult;
	private Question lastQuestion;
	private boolean pendingServer;
	private boolean finish;
	private List<Message> messages;
	private List<Answer> answers;

	public int getIdChat() {
		return idChat;
	}

	public void setIdChat(int idChat) {
		this.idChat = idChat;
	}

	public Update getUpdate() {
		return update;
	}

	public void setUpdate(Update update) {
		this.update = update;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public String getPostProcessedResult() {
		return postProcessedResult;
	}

	public void setProcessedResult(String postProcessedResult) {
		this.postProcessedResult = postProcessedResult;
	}

	public Question getLastQuestion() {
		return lastQuestion;
	}

	public void setLastQuestion(Question lastQuestion) {
		this.lastQuestion = lastQuestion;
	}

	public boolean isPendingServer() {
		return pendingServer;
	}

	public void setPendingServer(boolean pendingServer) {
		this.pendingServer = pendingServer;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

}
