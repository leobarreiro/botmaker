package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Command;
import org.javaleo.grandpa.ejb.entities.Question;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.pojos.Answer;
import org.javaleo.grandpa.ejb.pojos.Dialog;

@Local
public interface IQuestionBusiness extends Serializable {

	List<Question> listQuestionsFromCommand(Command command);

	Question getLastQuestionFromCommand(Command command);

	void saveQuestion(Question question) throws BusinessException;

	void dropQuestion(Question question) throws BusinessException;

	void upQuestionOrder(Question question) throws BusinessException;

	Question getNextQuestion(Command command, int lastOrder);

	boolean validateAnswer(Dialog dialog, Question question);

	void postProcessAnswer(Dialog dialog, Question question, Answer answer) throws BusinessException;

}
