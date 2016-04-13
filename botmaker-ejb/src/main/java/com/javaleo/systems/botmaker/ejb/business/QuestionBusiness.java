package com.javaleo.systems.botmaker.ejb.business;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Answer;
import com.javaleo.systems.botmaker.ejb.utils.BotMakerUtils;

@Stateless
public class QuestionBusiness implements IQuestionBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	@Inject
	private IPersistenceBasic<Question> persistence;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Question> listQuestionsFromCommand(Command command) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Question> cq = cb.createQuery(Question.class);
		Root<Question> fromQuestion = cq.from(Question.class);
		Join<Question, Command> joinCommand = fromQuestion.join("command", JoinType.INNER);
		cq.where(cb.equal(joinCommand.get("id"), command.getId()));
		cq.orderBy(cb.asc(fromQuestion.get("order")));
		cq.select(fromQuestion);
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
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
	public void saveQuestion(Question question) throws BusinessException {
		if (question.getId() == null) {
			Question previousQuestion = getLastQuestionFromCommand(question.getCommand());
			int thisOrder = (previousQuestion == null) ? 1 : (previousQuestion.getOrder() + 1);
			question.setOrder(thisOrder);
		}
		persistence.saveOrUpdate(question);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void upQuestionOrder(Question question) throws BusinessException {
		Integer actualOrder = question.getOrder();
		if (actualOrder == null || actualOrder == 0) {
			Question lastQuestion = getLastQuestionFromCommand(question.getCommand());
			Integer order = (lastQuestion == null) ? 1 : (lastQuestion.getOrder() + 1);
			question.setOrder(order);
			persistence.saveOrUpdate(question);
			return;
		} else if (actualOrder == 1) {
			throw new BusinessException("The question is already at top of the list.");
		} else {
			Question previous = getQuestionByCommandAndOrder(question.getCommand(), (actualOrder - 1));
			previous.setOrder(actualOrder);
			question.setOrder(actualOrder - 1);
			persistence.saveOrUpdate(previous);
			persistence.saveOrUpdate(question);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Question getQuestionByCommandAndOrder(Command command, int order) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Question> cq = cb.createQuery(Question.class);
		Root<Question> from = cq.from(Question.class);
		Join<Question, Command> joinCommand = from.join("command", JoinType.INNER);
		cq.where(cb.and(cb.equal(joinCommand.get("id"), command.getId()), cb.equal(from.get("order"), order)));
		cq.select(from);
		return persistence.getSingleResult(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Question getNextQuestion(Command command, int lastOrder) {
		return getQuestionByCommandAndOrder(command, (lastOrder + 1));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean validateAnswer(Question question, Answer answer) {
		if (question.getExpectedAnswer().getSnippetType().isScript()) {
			Pattern pattern = Pattern.compile(question.getExpectedAnswer().getRegularExpression());
			Matcher m = pattern.matcher(StringUtils.lowerCase(answer.getAnswer()));
			return m.matches();
		} else {
			return true;
		}
	}

	@Override
	public List<List<String>> convertOptions(Question question) {
		return BotMakerUtils.convertStringToArrayOfArrays(question.getExpectedAnswer().getRegularExpression(), 2, ',');
	}
}
