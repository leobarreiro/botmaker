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
public class ScriptRunnerUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	private void initialize() {
		// Policy.setPolicy(p);
		System.setSecurityManager(new ScriptSecurityManager());
	}

	@TransactionTimeout(unit = TimeUnit.MILLISECONDS, value = 15)
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@AccessTimeout(unit = TimeUnit.SECONDS, value = 10)
	public Object evaluateGroovy(String scriptGroovy, Binding binding) {
		// initialize();
		GroovyShell shell = new GroovyShell(binding);
		return shell.evaluate(scriptGroovy);
	}

}
