package org.javaleo.grandpa.ejb.pojos;

import java.io.Serializable;
import java.util.List;

import org.javaleo.grandpa.ejb.entities.Question;
import org.javaleo.grandpa.ejb.enums.AnswerType;

public class Answer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Question question;
	private String varName;
	private String postProcessedAnswer;
	private String answer;
	private AnswerType answerType;
	private List<UrlFile> urlFiles;
	private boolean accepted;

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getPostProcessedAnswer() {
		return postProcessedAnswer;
	}

	public void setPostProcessedAnswer(String processed) {
		this.postProcessedAnswer = processed;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public AnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}

	public List<UrlFile> getUrlFiles() {
		return urlFiles;
	}

	public void setUrlFiles(List<UrlFile> urlFiles) {
		this.urlFiles = urlFiles;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (accepted ? 1231 : 1237);
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((answerType == null) ? 0 : answerType.hashCode());
		result = prime * result + ((postProcessedAnswer == null) ? 0 : postProcessedAnswer.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((urlFiles == null) ? 0 : urlFiles.hashCode());
		result = prime * result + ((varName == null) ? 0 : varName.hashCode());
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
		Answer other = (Answer) obj;
		if (accepted != other.accepted)
			return false;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (answerType != other.answerType)
			return false;
		if (postProcessedAnswer == null) {
			if (other.postProcessedAnswer != null)
				return false;
		} else if (!postProcessedAnswer.equals(other.postProcessedAnswer))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (urlFiles == null) {
			if (other.urlFiles != null)
				return false;
		} else if (!urlFiles.equals(other.urlFiles))
			return false;
		if (varName == null) {
			if (other.varName != null)
				return false;
		} else if (!varName.equals(other.varName))
			return false;
		return true;
	}

}
