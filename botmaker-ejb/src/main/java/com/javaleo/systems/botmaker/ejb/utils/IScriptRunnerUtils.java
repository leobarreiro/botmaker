package com.javaleo.systems.botmaker.ejb.utils;

import java.io.Serializable;
import java.util.Map;

import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

public interface IScriptRunnerUtils extends Serializable {

	void validateScript(String script) throws BusinessException;

	Object testScript(Dialog dialog, String script, Map<String, String> contextVars) throws BusinessException;

	Object evaluateScript(Dialog dialog, String script) throws BusinessException;

}
