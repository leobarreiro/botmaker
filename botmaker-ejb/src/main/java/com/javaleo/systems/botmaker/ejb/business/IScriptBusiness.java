package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Script;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.pojos.DialogContextVar;

@Local
public interface IScriptBusiness extends Serializable {

	boolean isValidScript(Script script) throws BusinessException;

	String debugScript(Dialog dialog, Script script);

	String executeScript(Dialog dialog, Script script) throws BusinessException;

	Boolean evaluateBooleanScript(Dialog dialog, Script script) throws BusinessException;

	List<Script> listLastGenericScriptsFromUser();
	
	List<Script> listGenericScripts();
	
	void saveScript(Script script) throws BusinessException;

}
