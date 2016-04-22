package com.javaleo.systems.botmaker.ejb.pojos;

import java.io.Serializable;

import com.javaleo.systems.botmaker.ejb.entities.Question;

public class Answer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Question question;
	private String varName;
	private String codeProduced;
	private String answer;
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

	public String getCodeProduced() {
		return codeProduced;
	}

	public void setCodeProduced(String codeProduced) {
		this.codeProduced = codeProduced;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

}
