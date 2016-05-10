package com.javaleo.systems.botmaker.ejb.pojos;

import java.io.Serializable;

import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.enums.AnswerType;

public class Answer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Question question;
	private String varName;
	private String postProcessedAnswer;
	private String answer;
	private AnswerType answerType;
	private String url;
	private String fileId;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

}
