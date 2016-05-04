package com.javaleo.systems.botmaker.ejb.utils;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;

import org.jboss.ejb3.annotation.TransactionTimeout;

import com.javaleo.systems.botmaker.ejb.security.ScriptSecurityManager;

@Named
@Stateless
public class GroovyScriptRunnerUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object evaluateGroovy(String scriptGroovy, Binding binding) {
		// Policy.setPolicy(p);
		// System.setSecurityManager(new ScriptSecurityManager());
		GroovyShell shell = new GroovyShell(binding);
		return shell.evaluate(scriptGroovy);
	}

}
