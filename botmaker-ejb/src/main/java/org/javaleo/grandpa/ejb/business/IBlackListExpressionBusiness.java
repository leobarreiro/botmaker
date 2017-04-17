package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.BlackListExpression;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IBlackListExpressionBusiness extends Serializable {

	void saveBlackListExpression(BlackListExpression expression) throws BusinessException;

	List<BlackListExpression> listBlackListExpressionByScriptType(ScriptType scriptType);

	void testScriptAgainstBlackListExpression(String scriptCode, ScriptType scriptType) throws BusinessException;

	void dropBlackListExpression(BlackListExpression expression) throws BusinessException;

}
