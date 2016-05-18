package com.javaleo.systems.botmaker.ejb.pojos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	private long lastInteraction;
	private boolean pendingServer;
	private boolean pendingCommand;
	private List<Answer> answers;
	private Map<String, String> contextVars;

	public void addContextVar(String name, String value) {
		if (contextVars == null) {
			contextVars = new LinkedHashMap<String, String>();
		}
		contextVars.put(name, value);
	}

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

	public void setPostProcessedResult(String postProcessedResult) {
		this.postProcessedResult = postProcessedResult;
	}

	public Question getLastQuestion() {
		return lastQuestion;
	}

	public void setLastQuestion(Question lastQuestion) {
		this.lastQuestion = lastQuestion;
	}

	public long getLastInteraction() {
		return lastInteraction;
	}

	public void setLastInteraction(long lastInteraction) {
		this.lastInteraction = lastInteraction;
	}

	public boolean isPendingServer() {
		return pendingServer;
	}

	public void setPendingServer(boolean pendingServer) {
		this.pendingServer = pendingServer;
	}

	public boolean isPendingCommand() {
		return pendingCommand;
	}

	public void setPendingCommand(boolean pendingCommand) {
		this.pendingCommand = pendingCommand;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Map<String, String> getContextVars() {
		return (contextVars != null) ? contextVars : new HashMap<String, String>();
	}

	public void setContextVars(Map<String, String> contextVars) {
		this.contextVars = contextVars;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (pendingCommand ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + (pendingServer ? 1231 : 1237);
		result = prime * result + ((postProcessedResult == null) ? 0 : postProcessedResult.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dialog other = (Dialog) obj;
		if (pendingCommand != other.isPendingServer())
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
