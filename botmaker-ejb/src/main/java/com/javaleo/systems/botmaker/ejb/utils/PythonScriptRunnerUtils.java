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

import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

@Named
@Stateless
public class PythonScriptRunnerUtils implements Serializable {
	
	// implements IScriptRunnerUtils

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void validateScript(String script) throws BusinessException {
		// TODO definir expressoes nao permitidas para os scripts python.
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object testScript(Dialog dialog, String script, Map<String, String> contextVars) throws BusinessException {
		try {
			validateScript(script);
			PythonInterpreter py = new PythonInterpreter();
			mountBinding(py, contextVars);
			PyObject result = py.eval(script);
			return result;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new String(e.getMessage());
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object evaluateScript(Dialog dialog, String script) throws BusinessException {
		try {
			validateScript(script);
			PyObject dict = new PyObject(PyDictionary.TYPE);
			PythonInterpreter py = new PythonInterpreter();
			Map<String, String> contextVars = dialog.getContextVars();
			mountBinding(py, contextVars);
			PyObject result = py.eval(script);
			// TODO: popular contextvars do Dialog apos execucao do script python.
			return result;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}
	
	private void mountBinding(PythonInterpreter py, Map<String, String> contextVars) {
		for (String name : contextVars.keySet()) {
			py.set(name, new PyString(contextVars.get(name)));
		}
	}

}
