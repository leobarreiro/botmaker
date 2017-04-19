package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Script;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.pojos.Dialog;

@Local
public interface IScriptBusiness extends Serializable {

	boolean isValidScript(Script script) throws BusinessException;

	String debugScript(Dialog dialog, Script script);

	String executeScript(Dialog dialog, Script script) throws BusinessException;

	Boolean evaluateBooleanScript(Dialog dialog, Script script) throws BusinessException;

	Script getScriptToEdition(Long id);

	List<Script> listLastGenericScriptsFromUser();

	List<Script> listLastCommandScriptsEditedByUser();

	List<Script> listGenericScriptsFromScriptType(ScriptType scriptType);

	List<Script> listAllGenericScriptsFromCompany();

	void saveScript(Script script) throws BusinessException;

}
