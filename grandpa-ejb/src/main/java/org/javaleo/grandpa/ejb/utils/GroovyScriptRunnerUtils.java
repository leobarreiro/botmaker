package org.javaleo.grandpa.ejb.utils;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.business.bot.IBlackListExpressionBusiness;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.schedules.ManagerUtils;
import org.slf4j.Logger;

@Named
@Stateless
public class GroovyScriptRunnerUtils implements Serializable { // IScriptRunnerUtils

	private static final long serialVersionUID = 1L;

	@Inject
	private IBlackListExpressionBusiness blackListBusiness;

	@Inject
	private DevUtils devUtils;

	@Inject
	private ManagerUtils managerUtils;

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
			Map<String, Object> otherDialogsContextVars = managerUtils.getAllContextVarsFromUserIdAndBot(dialog.getBotId(), dialog.getUserId());
			Map<String, Object> actualDialogContextVars = dialog.getContextVars();

			Map<String, Object> allContextVars = new LinkedHashMap<String, Object>();
			allContextVars.putAll(otherDialogsContextVars);
			allContextVars.putAll(actualDialogContextVars);

			Binding binding = mountBinding(allContextVars);
			GroovyShell shell = new GroovyShell(binding);
			Object result = shell.evaluate(script);
			Binding postBinding = shell.getContext();
			for (Object key : postBinding.getVariables().keySet()) {
				String varName = (String) key;
				if (postBinding.getVariable(varName) instanceof Integer) {
					allContextVars.put(varName, Integer.toString((Integer) postBinding.getVariable(varName)));
				} else if (postBinding.getVariable(varName) instanceof String) {
					allContextVars.put(varName, (String) postBinding.getVariable(varName));
				} else if (postBinding.getVariable(varName) instanceof Long) {
					allContextVars.put(varName, Long.toString((Long) postBinding.getVariable(varName)));
				}
			}
			dialog.setContextVars(allContextVars);
			return result;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	private Binding mountBinding(Map<String, Object> contextVars) {
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
