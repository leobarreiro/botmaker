package com.javaleo.systems.botrise.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botrise.ejb.entities.Command;
import com.javaleo.systems.botrise.ejb.entities.Question;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;

@Local
public interface IQuestionBusiness extends Serializable {

	List<Question> listQuestionsByCommand(Command command);

	void saveQuestion(Question question) throws BotRiseException;

}
