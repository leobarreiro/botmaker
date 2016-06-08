package com.javaleo.systems.botmaker.ejb.business;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.entities.Script;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.utils.GroovyScriptRunnerUtils;
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
	private GroovyScriptRunnerUtils groovyRunner;

	@Inject
	private PythonScriptRunnerUtils pythonRunner;

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
		if (script.getScriptType().equals(ScriptType.GROOVY)) {
			result = (String) groovyRunner.evaluateScript(dialog, script.getCode());
		} else { // ScriptType.PYTHON
			result = (String) pythonRunner.evaluateScript(dialog, script.getCode());
		}
		return result;
	}

	@Override
	public String debugScript(Dialog dialog, Script script) {
		try {
			isValidScript(script);
			String result = "";
			if (script.getScriptType().equals(ScriptType.GROOVY)) {
				result = (String) groovyRunner.testScript(dialog, script.getCode());
			} else { // ScriptType.PYTHON
				result = (String) pythonRunner.testScript(dialog, script.getCode());
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
		if (script.getScriptType().equals(ScriptType.GROOVY)) {
			result = (Boolean) groovyRunner.evaluateScript(dialog, script.getCode());
		} else { // ScriptType.PYTHON
			result = (Boolean) pythonRunner.evaluateScript(dialog, script.getCode());
		}
		return result;
	}

}
