package com.javaleo.systems.botrise.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
	public List<Question> listQuestionsFromCommand(Command command) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Question> cq = cb.createQuery(Question.class);
		Root<Question> fromQuestion = cq.from(Question.class);
		Join<Question, Command> joinCommand = fromQuestion.join("command", JoinType.INNER);
		cq.where(cb.equal(joinCommand.get("id"), command.getId()));
		cq.select(fromQuestion);
		return persistence.getResultList(cq);
	}

	@Override
	public Question getLastQuestionFromCommand(Command command) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Question> cq = cb.createQuery(Question.class);
		Root<Question> from = cq.from(Question.class);
		Join<Question, Command> joinCommand = from.join("command", JoinType.INNER);
		cq.where(cb.equal(joinCommand.get("id"), command.getId()));
		cq.orderBy(cb.desc(from.get("order")));
		cq.select(from);
		return persistence.getSingleResult(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveQuestion(Question question) throws BotRiseException {
		Question previousQuestion = getLastQuestionFromCommand(question.getCommand());
		int thisOrder = (previousQuestion == null) ? 1 : previousQuestion.getOrder();
		question.setOrder(thisOrder);
		persistence.saveOrUpdate(question);
	}

}
