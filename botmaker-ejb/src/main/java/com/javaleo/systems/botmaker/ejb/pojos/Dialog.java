package com.javaleo.systems.botmaker.ejb.pojos;

import java.io.Serializable;
import java.util.List;

import org.javaleo.libs.botgram.model.Message;
import org.javaleo.libs.botgram.model.Update;

import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;

public class Dialog implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Update lastUpdate;
	private Command lastCommand;
	private String postProcessedResult;
	private Question lastQuestion;
	private boolean pendingServer;
	private boolean finish;
	private List<Message> messages;
	private List<Answer> answers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Update getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Update lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Command getLastCommand() {
		return lastCommand;
	}

	public void setLastCommand(Command lastCommand) {
		this.lastCommand = lastCommand;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (finish ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + (pendingServer ? 1231 : 1237);
		result = prime * result + ((postProcessedResult == null) ? 0 : postProcessedResult.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dialog other = (Dialog) obj;
		if (finish != other.finish)
			return false;
		if (id != other.id)
			return false;
		if (pendingServer != other.pendingServer)
			return false;
		if (postProcessedResult == null) {
			if (other.postProcessedResult != null)
				return false;
		} else if (!postProcessedResult.equals(other.postProcessedResult))
			return false;
		return true;
	}

}
