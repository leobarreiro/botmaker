package com.javaleo.systems.botmaker.ejb.utils;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.Serializable;
import java.security.Policy;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.security.BotMakerScriptPolicy;
import com.javaleo.systems.botmaker.ejb.security.ScriptSecurityManager;

@Named
@Stateless
public class GroovyScriptRunnerUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object evaluateGroovy(String scriptGroovy, Binding binding) throws BusinessException {
		try {
			// String osName = System.getenv("os.name");
			// Policy.setPolicy(p);
			Policy.setPolicy(new BotMakerScriptPolicy());
			//System.setSecurityManager(new ScriptSecurityManager());
			GroovyShell shell = new GroovyShell(binding);
			return shell.evaluate(scriptGroovy);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

}
