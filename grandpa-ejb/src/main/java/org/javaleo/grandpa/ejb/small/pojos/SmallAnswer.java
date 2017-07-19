package org.javaleo.grandpa.ejb.small.pojos;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.javaleo.grandpa.ejb.entities.bot.Question;
import org.javaleo.grandpa.ejb.enums.AnswerType;
import org.javaleo.grandpa.ejb.pojos.UrlFile;

@XmlRootElement(name = "small-answer")
@XmlAccessorType(XmlAccessType.FIELD)
public class SmallAnswer implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlTransient
	private Question question;

	@XmlTransient
	private String varName;

	@XmlTransient
	private String postProcessedAnswer;

	@XmlAttribute(name = "answer")
	private String answer;

	@XmlAttribute(name = "answer-type")
	private AnswerType answerType;

	@XmlTransient
	private List<UrlFile> urlFiles;

	@XmlAttribute(name = "accepted")
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

	public void setPostProcessedAnswer(String postProcessedAnswer) {
		this.postProcessedAnswer = postProcessedAnswer;
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

}
