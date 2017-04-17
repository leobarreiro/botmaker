package org.javaleo.grandpa.ejb.business;

import java.text.MessageFormat;
import java.util.Calendar;
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

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Command;
import org.javaleo.grandpa.ejb.entities.Question;
import org.javaleo.grandpa.ejb.entities.Script;
import org.javaleo.grandpa.ejb.enums.AnswerType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.pojos.Answer;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.libs.botgram.model.Document;
import org.javaleo.libs.botgram.model.PhotoSize;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.slf4j.Logger;

@Stateless
public class QuestionBusiness implements IQuestionBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	@Inject
	private IPersistenceBasic<Question> persistence;

	@Inject
	private IPersistenceBasic<Script> scriptPersistence;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private IScriptBusiness scriptBiz;

	@Inject
	private IValidatorBusiness validatorBusiness;

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
		Calendar cal = Calendar.getInstance();
		if (question.getProcessAnswer() && question.getPostScript() != null && StringUtils.isNotEmpty(question.getPostScript().getCode())) {
			Script script = question.getPostScript();
			if (script.getId() == null) {
				script.setAuthor(credentials.getUser());
				script.setCreated(cal.getTime());
			}
			script.setModified(cal.getTime());
			// TODO: testar validade para outros tipos de linguagens de script alem de groovy
			script.setValid(scriptBiz.isValidScript(script));
			script.setEnabled(scriptBiz.isValidScript(script));
			script.setQuestion(question);
			question.setPostScript(script);
			scriptPersistence.saveOrUpdate(script);
		} else {
			question.setPostScript(null);
		}
		persistence.saveOrUpdate(question);
		persistence.getEntityManager().flush();
	}

	@Override
	public void dropQuestion(Question question) throws BusinessException {
		Question deleteQuestion = persistence.find(Question.class, question.getId());
		persistence.remove(deleteQuestion);
		persistence.getEntityManager().flush();
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
	public boolean validateAnswer(Dialog dialog, Question question) {
		if (AnswerType.PHOTO.equals(question.getAnswerType())) {
			List<PhotoSize> photoSizes = dialog.getLastUpdate().getMessage().getPhotosizes();
			return (photoSizes != null && !photoSizes.isEmpty());
		} else if (AnswerType.DOCUMENT.equals(question.getAnswerType())) {
			Document document = dialog.getLastUpdate().getMessage().getDocument();
			return (document != null && document.getSize() > 0);
		} else if (AnswerType.STRING.equals(question.getAnswerType()) || AnswerType.NUMERIC.equals(question.getAnswerType())) {
			if (question.getValidator() == null || question.getValidator().getValidatorType() == null) {
				return true;
			} else {
				try {
					return validatorBusiness.validateContent(question.getValidator(), dialog.getLastUpdate().getMessage().getText());
				} catch (BusinessException e) {
					LOG.warn(MessageFormat.format("Error when trying to validate an answer. Msg: {0}", e.getMessage()));
					return false;
				}
			}
		}
		return false;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void postProcessAnswer(Dialog dialog, Question question, Answer answer) throws BusinessException {
		if (!scriptBiz.isValidScript(question.getPostScript())) {
			throw new BusinessException(
					MessageFormat.format("Trying to execute a Post Script not valid [Bot:{0}|Command:{1}]", question.getCommand().getBot().getId().toString(),
							question.getCommand().getId().toString(), question.getId().toString()));
		}
		String postProcessed = scriptBiz.executeScript(dialog, question.getPostScript());
		dialog.setPostProcessedResult(postProcessed);
		answer.setPostProcessedAnswer(postProcessed);
	}

}
