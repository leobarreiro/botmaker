package com.javaleo.systems.botmaker.ejb.utils;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.business.IBlackListExpressionBusiness;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

@Named
@Stateless
public class GroovyScriptRunnerUtils implements Serializable { // IScriptRunnerUtils

	private static final long serialVersionUID = 1L;

	@Inject
	private IBlackListExpressionBusiness blackListBusiness;
	
	@Inject
	private DevUtils devUtils;

	@Inject
	private Logger LOG;


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object testScript(Dialog dialog, String script) throws BusinessException {
		try {
			blackListBusiness.testScriptAgainstBlackListExpression(script, ScriptType.GROOVY);
			Binding binding = mountBinding(dialog.getContextVars());
			devUtils.configureBotAndDialog(dialog.getBotId(), dialog.getId());
			GroovyShell shell = new GroovyShell(binding);
			Object result = shell.evaluate(script);
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
			blackListBusiness.testScriptAgainstBlackListExpression(script, ScriptType.GROOVY);
			Map<String, String> contextVars = dialog.getContextVars();
			Binding binding = mountBinding(contextVars);
			GroovyShell shell = new GroovyShell(binding);
			Object result = shell.evaluate(script);
			Binding postBinding = shell.getContext();
			for (Object key : postBinding.getVariables().keySet()) {
				String varName = (String) key;
				if (postBinding.getVariable(varName) instanceof Integer) {
					contextVars.put(varName, Integer.toString((Integer) postBinding.getVariable(varName)));
				} else {
					contextVars.put(varName, (String) postBinding.getVariable(varName));
				}
			}
			dialog.setContextVars(contextVars);
			return result;
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
