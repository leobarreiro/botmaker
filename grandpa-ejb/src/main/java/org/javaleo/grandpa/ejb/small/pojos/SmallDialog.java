package org.javaleo.grandpa.ejb.small.pojos;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.javaleo.grandpa.ejb.entities.Command;
import org.javaleo.grandpa.ejb.entities.Question;

@XmlRootElement(name = "small-dialog")
@XmlAccessorType(XmlAccessType.FIELD)
public class SmallDialog implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id")
	private int id;

	@XmlElement(name = "bot-id")
	private Long botId;

	@XmlElement(name = "user-id")
	private Integer userId;

	@XmlAttribute(name = "last-command")
	private Command lastCommand;

	@XmlTransient
	private String postProcessedResult;

	@XmlTransient
	private Question lastQuestion;

	@XmlAttribute(name = "last-interaction")
	private long lastInteraction;

	@XmlTransient
	private boolean pendingServer;

	@XmlTransient
	private boolean pendingCommand;

	@XmlAttribute(name = "small-answers")
	private List<SmallAnswer> answers;

	@XmlTransient
	private Map<String, Object> contextVars;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getBotId() {
		return botId;
	}

	public void setBotId(Long botId) {
		this.botId = botId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public List<SmallAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<SmallAnswer> answers) {
		this.answers = answers;
	}

	public Map<String, Object> getContextVars() {
		return contextVars;
	}

	public void setContextVars(Map<String, Object> contextVars) {
		this.contextVars = contextVars;
	}

}
