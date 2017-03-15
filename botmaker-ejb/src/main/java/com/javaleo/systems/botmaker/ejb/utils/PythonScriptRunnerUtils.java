package com.javaleo.systems.botmaker.ejb.utils;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.business.IBlackListExpressionBusiness;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

@Named
@Stateless
public class PythonScriptRunnerUtils implements Serializable {

	// implements IScriptRunnerUtils

	private static final long serialVersionUID = 1L;

	@Inject
	private IBlackListExpressionBusiness blackListBusiness;

	@Inject
	private Logger LOG;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object testScript(Dialog dialog, String script) throws BusinessException {
		blackListBusiness.testScriptAgainstBlackListExpression(script, ScriptType.PYTHON);
		try {
			PythonInterpreter py = new PythonInterpreter();
			mountBinding(py, dialog.getContextVars());
			PyObject result = py.eval(script);
			return result;
		} catch (Throwable e) {
			LOG.error(e.getMessage());
			return new String(e.toString());
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object evaluateScript(Dialog dialog, String script) throws BusinessException {
		blackListBusiness.testScriptAgainstBlackListExpression(script, ScriptType.PYTHON);
		try {
			// PyObject dict = new PyObject(PyDictionary.TYPE);
			PythonInterpreter py = new PythonInterpreter();
			Map<String, Object> contextVars = dialog.getContextVars();
			mountBinding(py, contextVars);
			py.exec(script);
			PyObject result = py.get("response");
			py.cleanup();
			// TODO: popular contextvars do Dialog apos execucao do script python.
			return result.toString();
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	private void mountBinding(PythonInterpreter py, Map<String, Object> contextVars) {
		for (String name : contextVars.keySet()) {
			py.set(name, contextVars.get(name));
		}
	}

}
