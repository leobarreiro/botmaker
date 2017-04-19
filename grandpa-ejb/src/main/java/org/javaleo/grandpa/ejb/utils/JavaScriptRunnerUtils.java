package org.javaleo.grandpa.ejb.utils;

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
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.javaleo.grandpa.ejb.business.IBlackListExpressionBusiness;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.schedules.ManagerUtils;
import org.slf4j.Logger;

@Named
@Stateless
public class JavaScriptRunnerUtils implements Serializable {

	private static final long serialVersionUID = 865904838669999842L;

	@Inject
	private ManagerUtils managerUtils;

	@Inject
	private IBlackListExpressionBusiness blackListExpressionBusiness;

	@Inject
	private Logger LOG;

	private ScriptEngineManager engineManager;
	private ScriptEngine scriptEngine;

	public void initialize() {
		engineManager = new ScriptEngineManager();
		scriptEngine = engineManager.getEngineByName("JavaScript");

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object testScript(Dialog dialog, String script) throws BusinessException {
		initialize();
		try {
			blackListExpressionBusiness.testScriptAgainstBlackListExpression(script, ScriptType.JAVASCRIPT);
			Bindings bindings = mountBinding(dialog.getContextVars());
			scriptEngine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
			return scriptEngine.eval(script);
		} catch (ScriptException e) {
			LOG.warn(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object evaluateScript(Dialog dialog, String script) throws BusinessException {
		initialize();
		try {
			blackListExpressionBusiness.testScriptAgainstBlackListExpression(script, ScriptType.JAVASCRIPT);
			Map<String, Object> otherDialogsContextVars = managerUtils.getAllContextVarsFromUserIdAndBot(dialog.getBotId(), dialog.getUserId());
			Map<String, Object> actualDialogContextVars = dialog.getContextVars();

			Map<String, Object> allContextVars = new LinkedHashMap<String, Object>();
			allContextVars.putAll(otherDialogsContextVars);
			allContextVars.putAll(actualDialogContextVars);

			Bindings bindings = mountBinding(allContextVars);
			scriptEngine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

			Object result = scriptEngine.eval(script);
			Bindings postBindings = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);

			for (Object key : postBindings.keySet()) {
				String varName = (String) key;
				if (postBindings.get(varName) instanceof Integer) {
					allContextVars.put(varName, Integer.toString((Integer) postBindings.get(varName)));
				} else if (postBindings.get(varName) instanceof String) {
					allContextVars.put(varName, (String) postBindings.get(varName));
				} else if (postBindings.get(varName) instanceof Long) {
					allContextVars.put(varName, Long.toString((Long) postBindings.get(varName)));
				}
			}
			dialog.setContextVars(allContextVars);
			return result;

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	private Bindings mountBinding(Map<String, Object> contextVars) {
		Bindings bindings = scriptEngine.createBindings();
		bindings.putAll(contextVars);
		return bindings;
	}

}
