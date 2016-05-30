package com.javaleo.systems.botmaker.ejb.business;

import java.text.MessageFormat;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.entities.Script;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.utils.GroovyScriptRunnerUtils;

@Stateless
public class ScriptBusiness implements IScriptBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	@Inject
	private GroovyScriptRunnerUtils groovyRunner;

	@Override
	public boolean isReadyToExecution(Script script) {
		return (script != null && script.getEnabled() && script.getValid() && StringUtils.isNotEmpty(script.getCode()));
	}

	@Override
	public boolean isValidScript(Script script) {
		try {
			groovyRunner.validateScript(script.getCode());
			return true;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		}
	}

	@Override
	public String executeScript(Dialog dialog, Script script) throws BusinessException {
		if (script == null) {
			throw new BusinessException(MessageFormat.format("Trying to execute an empty script [Bot:{0}|Command:{1}]", dialog.getBotId().toString(), dialog.getLastCommand().getId().toString()));
		}
		if (!isReadyToExecution(script)) {
			throw new BusinessException(MessageFormat.format("Script is not ready to execution [Id:{0}|Valid:{1}|Enabled:{2}|Author:{3}]", script.getId(), script.getValid(), script.getEnabled(),
					script.getAuthor().getId()));
		}
		groovyRunner.validateScript(script.getCode());
		return (String) groovyRunner.evaluateScript(dialog, script.getCode());
	}

	@Override
	public Boolean evaluateBooleanScript(Dialog dialog, Script script) throws BusinessException {
		if (!isReadyToExecution(script)) {
			throw new BusinessException(MessageFormat.format("Script is not ready to execution [Id:{0}|Valid:{1}|Enabled:{2}|Author:{3}]", script.getId(), script.getValid(), script.getEnabled(),
					script.getAuthor().getId()));
		}
		return (Boolean) groovyRunner.evaluateScript(dialog, script.getCode());
	}

}
