package com.javaleo.systems.botmaker.ejb.utils;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.Serializable;
import java.util.Iterator;
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
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.DialogContextVar;

@Named
@Stateless
public class GroovyScriptRunnerUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void validateScript(String script) throws BusinessException {
		if (StringUtils.containsIgnoreCase(script, "System.exit") || StringUtils.containsIgnoreCase(script, "System.exec")) {
			throw new BusinessException("Instructions not allowed in script source. It will not be executed.");
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object testScript(String script, Map<String, String> contextVars) throws BusinessException {
		try {
			validateScript(script);
			Binding binding = mountBinding(contextVars);
			GroovyShell shell = new GroovyShell(binding);
			return shell.evaluate(script);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new String(e.getMessage());
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object evaluateScript(String script, Map<String, String> contextVars) throws BusinessException {
		try {
			validateScript(script);
			Binding binding = mountBinding(contextVars);
			GroovyShell shell = new GroovyShell(binding);
			return shell.evaluate(script);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	private Binding mountBinding(Map<String, String> contextVars) {
		Binding binding = new Binding();
		for (String name : contextVars.keySet()) {
			binding.setVariable(name, contextVars.get(name));
		}
		return binding;
	}

	// String osName = System.getenv("os.name");
	// Policy.setPolicy(p);
	// Policy.setPolicy(new BotMakerScriptPolicy());
	// System.setSecurityManager(new ScriptSecurityManager());
}
