package com.javaleo.systems.botmaker.ejb.business;

import java.util.List;
import java.util.Map;
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
import org.javaleo.libs.botgram.model.Document;
import org.javaleo.libs.botgram.model.PhotoSize;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.enums.AnswerType;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.enums.ValidatorType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Answer;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.utils.BotMakerUtils;
import com.javaleo.systems.botmaker.ejb.utils.GroovyScriptRunnerUtils;

@Stateless
public class QuestionBusiness implements IQuestionBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	@Inject
	private IPersistenceBasic<Question> persistence;

	@Inject
	private GroovyScriptRunnerUtils scriptRunner;

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
	public boolean validateAnswer(Dialog dialog, Question question) {
		if (question.getAnswerType().equals(AnswerType.PHOTO)) {
			List<PhotoSize> photoSizes = dialog.getLastUpdate().getMessage().getPhotosizes();
			return (photoSizes != null && !photoSizes.isEmpty());
		} else if (question.getAnswerType().equals(AnswerType.DOCUMENT)) {
			Document document = dialog.getLastUpdate().getMessage().getDocument();
			return (document != null && document.getSize() > 0);
		} else {
			if (question.getValidator() == null || question.getValidator().getValidatorType() == null) {
				return true;
			} else {
				if (question.getValidator().getValidatorType().equals(ValidatorType.REGEXP)) {
					Pattern pattern = Pattern.compile(question.getValidator().getScriptCode());
					Matcher m = pattern.matcher(StringUtils.lowerCase(dialog.getLastUpdate().getMessage().getText()));
					return m.matches();
				} else if (question.getValidator().getValidatorType().equals(ValidatorType.GROOVY)) {
					Map<String, String> contextVars = dialog.getContextVars();
					contextVars.put("userAnswer", dialog.getLastUpdate().getMessage().getText());
					try {
						Boolean valid = (Boolean) scriptRunner.evaluateScript(question.getValidator().getScriptCode(), contextVars);
						return valid;
					} catch (Exception e) {
						LOG.error(e.getMessage());
						return false;
					}
				} else {
					return true;
				}
			}
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void postProcessAnswer(Dialog dialog, Question question, Answer answer) {
		if (question.getProcessAnswer()) {
			if (question.getScriptType().equals(ScriptType.GROOVY)) {
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.excludeFieldsWithoutExposeAnnotation();
				Gson gson = gsonBuilder.create();
				for (Answer a : dialog.getAnswers()) {
					if (a.isAccepted()) {
						if (a.getAnswerType().equals(AnswerType.DOCUMENT)) {
							String content = gson.toJson(a.getUrlFiles().get(0));
							dialog.addContextVar(a.getVarName(), content);
						} else if (a.getAnswerType().equals(AnswerType.PHOTO)) {
							String content = gson.toJson(a.getUrlFiles());
							dialog.addContextVar(a.getVarName(), content);
						} else { // a.getAnswer().equals(AnswerType.STRING) || a.getAnswer().equals(AnswerType.NUMERIC)
							dialog.addContextVar(a.getVarName(), a.getAnswer());
						}
					}
				}
				try {
					String postProcessed = (String) scriptRunner.evaluateScript(question.getPostProcessScript(), dialog.getContextVars());
					answer.setPostProcessedAnswer(postProcessed);
				} catch (BusinessException e) {
					LOG.error(e.getMessage());
				}
			}
		}
	}

	@Override
	public List<List<String>> convertOptions(Question question) {
		return BotMakerUtils.convertStringToArrayOfArrays(question.getValidator().getScriptCode(), 2, ',');
	}
}
