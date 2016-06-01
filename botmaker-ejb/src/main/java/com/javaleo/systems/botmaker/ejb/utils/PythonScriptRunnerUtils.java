package com.javaleo.systems.botmaker.ejb.utils;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
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
		List<String> blackListSnippets = new ArrayList<String>();
		blackListSnippets.add("import os");
		blackListSnippets.add("import sys");
		blackListSnippets.add("open(");
		blackListSnippets.add("file(");
		blackListSnippets.add("import threading");
		blackListSnippets.add("__import__('threading')");
		blackListSnippets.add("os.environ.get");
		blackListSnippets.add("eval(");
		blackListSnippets.add("exec(");
		for (String snippet : blackListSnippets) {
			if (StringUtils.containsIgnoreCase(script, snippet)) {
				throw new BusinessException(MessageFormat.format("Instruction not allowed in script source [{0}]. It will not be executed.", snippet));
			}
		}
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
			py.exec(script);
			PyObject result = py.get("result");
			py.cleanup();
			// TODO: popular contextvars do Dialog apos execucao do script python.
			return result.toString();
		} catch (Exception e) {
			LOG.error(e.getMessage());
			// e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	private void mountBinding(PythonInterpreter py, Map<String, String> contextVars) {
		for (String name : contextVars.keySet()) {
			py.set(name, contextVars.get(name));
		}
	}

}
