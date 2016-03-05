package com.javaleo.systems.botrise.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botrise.ejb.entities.Command;
import com.javaleo.systems.botrise.ejb.entities.Question;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;

@Stateless
public class QuestionBusiness implements IQuestionBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Question> persistence;

	@Override
	public List<Question> listQuestionsByCommand(Command command) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Question> cq = cb.createQuery(Question.class);
		Root<Question> fromQuestion = cq.from(Question.class);
		Join<Question, Command> joinCommand = fromQuestion.join("command", JoinType.INNER);
		cq.where(cb.equal(joinCommand.get("id"), command.getId()));
		cq.select(fromQuestion);
		return persistence.getResultList(cq);
	}

	@Override
	public void saveQuestion(Question question) throws BotRiseException {
		persistence.saveOrUpdate(question);
	}

}
