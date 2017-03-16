package com.javaleo.systems.botmaker.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.python.icu.util.Calendar;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.entities.Script;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.security.BotMakerCredentials;
import com.javaleo.systems.botmaker.ejb.utils.GroovyScriptRunnerUtils;
import com.javaleo.systems.botmaker.ejb.utils.JavaScriptRunnerUtils;
import com.javaleo.systems.botmaker.ejb.utils.PythonScriptRunnerUtils;

@Named
@Stateless
public class ScriptBusiness implements IScriptBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	@Inject
	private IBlackListExpressionBusiness blackListBusiness;

	@Inject
	private IPersistenceBasic<Script> persistence;

	@Inject
	private IPersistenceBasic<Command> commandPersistence;

	@Inject
	private IPersistenceBasic<Question> questionPersistence;

	@Inject
	private IPersistenceBasic<Validator> validatorPersistence;

	@Inject
	private GroovyScriptRunnerUtils groovyRunner;

	@Inject
	private PythonScriptRunnerUtils pythonRunner;

	@Inject
	private JavaScriptRunnerUtils javascriptRunner;

	@Inject
	private BotMakerCredentials credentials;

	@Override
	public boolean isValidScript(Script script) throws BusinessException {
		if (StringUtils.isBlank(script.getCode())) {
			throw new BusinessException("Script code is empty.");
		}
		if (script.getScriptType() == null) {
			throw new BusinessException("Script type is null.");
		}
		blackListBusiness.testScriptAgainstBlackListExpression(script.getCode(), script.getScriptType());
		return true;
	}

	@Override
	public String executeScript(Dialog dialog, Script script) throws BusinessException {
		isValidScript(script);
		String result = "";
		String code = getCodeFromScript(script);
		// ScriptType.GROOVY
		if (ScriptType.GROOVY.equals(script.getScriptType())) {
			result = (String) groovyRunner.evaluateScript(dialog, code);
		}
		// ScriptType.PYTHON
		else if (ScriptType.PYTHON.equals(script.getScriptType())) {
			result = (String) pythonRunner.evaluateScript(dialog, code);
		}
		// ScriptType.JAVASCRIPT
		else {
			result = (String) javascriptRunner.evaluateScript(dialog, code);
		}
		return result;
	}

	@Override
	public String debugScript(Dialog dialog, Script script) {
		try {
			isValidScript(script);
			String result = "";
			String code = getCodeFromScript(script);
			// ScriptType.GROOVY
			if (ScriptType.GROOVY.equals(script.getScriptType())) {
				result = (String) groovyRunner.testScript(dialog, code);
			}
			// ScriptType.PYTHON
			else if (ScriptType.PYTHON.equals(script.getScriptType())) {
				result = (String) pythonRunner.testScript(dialog, code);
			}
			// ScriptType.JAVASCRIPT
			else {
				result = (String) javascriptRunner.testScript(dialog, code);
			}
			return result;
		} catch (Throwable e) {
			return e.getMessage();
		}
	}

	@Override
	public Boolean evaluateBooleanScript(Dialog dialog, Script script) throws BusinessException {
		isValidScript(script);
		Boolean result = false;
		String code = getCodeFromScript(script);
		if (ScriptType.GROOVY.equals(script.getScriptType())) {
			result = (Boolean) groovyRunner.evaluateScript(dialog, code);
		}
		// ScriptType.PYTHON
		else if (ScriptType.PYTHON.equals(script.getScriptType())) {
			result = (Boolean) pythonRunner.evaluateScript(dialog, code);
		}
		// ScriptType.JAVASCRIPT
		else {
			result = (Boolean) javascriptRunner.evaluateScript(dialog, code);
		}
		return result;
	}

	private String getCodeFromScript(Script script) {
		StringBuilder strCode = new StringBuilder();
		if (script.getGenericScript() != null && script.getScriptType().equals(script.getGenericScript().getScriptType())) {
			strCode.append("\n\n// Start of Generic Script\n\n");
			strCode.append(script.getGenericScript().getCode());
			strCode.append("\n\n// End of Generic Script\n\n");
		}
		strCode.append(script.getCode());
		return strCode.toString();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Script getScriptToEdition(Long id) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Script> cq = cb.createQuery(Script.class);
		Root<Script> from = cq.from(Script.class);
		from.join("genericScript", JoinType.LEFT);
		from.join("command", JoinType.LEFT);
		from.join("question", JoinType.LEFT);
		from.join("validator", JoinType.LEFT);
		cq.where(cb.equal(from.get("id"), id));
		Script script = persistence.getSingleResult(cq);
		persistence.getEntityManager().contains(script);
		return script;
	}

	@Override
	public List<Script> listLastGenericScriptsFromUser() {
		if (credentials.getCompany() == null) {
			return null;
		}
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Script> cq = cb.createQuery(Script.class);
		Root<Script> from = cq.from(Script.class);
		Join<Script, User> joinAuthor = from.join("author", JoinType.INNER);
		Join<User, Company> joinCompany = joinAuthor.join("company", JoinType.INNER);
		Predicate whereCompany = cb.equal(joinCompany.get("id"), credentials.getCompany().getId());
		Predicate whereGeneric = cb.equal(from.<Boolean> get("generic"), true);
		cq.where(cb.and(whereCompany, whereGeneric));
		cq.orderBy(cb.desc(from.get("id")), cb.asc(from.get("name")));
		cq.select(from);
		return persistence.getResultList(cq);
	}

	@Override
	public List<Script> listGenericScriptsFromScriptType(ScriptType scriptType) {
		if (credentials.getCompany() == null) {
			return null;
		}
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Script> cq = cb.createQuery(Script.class);
		Root<Script> from = cq.from(Script.class);
		Join<Script, User> joinAuthor = from.join("author", JoinType.INNER);
		Join<User, Company> joinCompany = joinAuthor.join("company", JoinType.INNER);
		Predicate whereCompany = cb.equal(joinCompany.get("id"), credentials.getCompany().getId());
		Predicate whereGeneric = cb.equal(from.<Boolean> get("generic"), true);
		Predicate whereScriptType = cb.equal(from.get("scriptType"), scriptType);
		cq.where(cb.and(whereCompany, whereGeneric, whereScriptType));
		cq.orderBy(cb.asc(from.get("name")));
		cq.select(from);
		return persistence.getResultList(cq);
	}

	@Override
	public List<Script> listAllGenericScriptsFromCompany() {
		if (credentials.getCompany() == null) {
			return null;
		}
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Script> cq = cb.createQuery(Script.class);
		Root<Script> from = cq.from(Script.class);
		Join<Script, User> joinAuthor = from.join("author", JoinType.INNER);
		Join<User, Company> joinCompany = joinAuthor.join("company", JoinType.INNER);
		Predicate whereCompany = cb.equal(joinCompany.get("id"), credentials.getCompany().getId());
		Predicate whereGeneric = cb.equal(from.<Boolean> get("generic"), true);
		cq.where(cb.and(whereCompany, whereGeneric));
		cq.orderBy(cb.asc(from.get("name")));
		cq.select(from);
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveScript(Script script) throws BusinessException {

		persistence.getEntityManager().contains(script);

		if (script.getGeneric() != null && script.getGeneric()) {
			if (StringUtils.isBlank(script.getName())) {
				throw new BusinessException("For Generic Script is mandatory enter a Name.");
			}
		} else {
			if (script.getCommand() == null && script.getQuestion() == null && script.getValidator() == null) {
				throw new BusinessException("The script must be owned by a command or a question or a validator to be saved.");
			}
		}

		if (script.getCommand() != null && script.getCommand().getId() != null) {
			Command command = commandPersistence.find(Command.class, script.getCommand().getId());
			script.setCommand(command);
			command.setPostScript(script);
		}

		if (script.getQuestion() != null && script.getQuestion().getId() != null) {
			Question question = questionPersistence.find(Question.class, script.getQuestion().getId());
			script.setQuestion(question);
			question.setPostScript(script);
		}

		if (script.getValidator() != null && script.getValidator().getId() != null) {
			Validator validator = validatorPersistence.find(Validator.class, script.getValidator().getId());
			script.setValidator(validator);
			validator.setScript(script);
		}

		if (script.getAuthor() == null) {
			script.setAuthor(credentials.getUser());
		}

		Calendar cal = Calendar.getInstance();
		if (script.getCreated() == null) {
			script.setCreated(cal.getTime());
		}
		script.setModified(cal.getTime());

		persistence.saveOrUpdate(script);
	}

}
