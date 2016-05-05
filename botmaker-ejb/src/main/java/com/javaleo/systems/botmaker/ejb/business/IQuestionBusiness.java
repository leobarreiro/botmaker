package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Answer;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

@Local
public interface IQuestionBusiness extends Serializable {

	List<Question> listQuestionsFromCommand(Command command);

	Question getLastQuestionFromCommand(Command command);

	void saveQuestion(Question question) throws BusinessException;

	void upQuestionOrder(Question question) throws BusinessException;

	Question getNextQuestion(Command command, int lastOrder);

	boolean validateAnswer(Dialog dialog, Question question, Answer answer);

	List<List<String>> convertOptions(Question question);

	void postProcessAnswer(Dialog dialog, Question question, Answer answer);

}
