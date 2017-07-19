package org.javaleo.grandpa.ejb.business.bot;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.bot.BlackListExpression;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IBlackListExpressionBusiness extends Serializable {

	void saveBlackListExpression(BlackListExpression expression) throws BusinessException;

	List<BlackListExpression> listBlackListExpressionByScriptType(ScriptType scriptType);

	void testScriptAgainstBlackListExpression(String scriptCode, ScriptType scriptType) throws BusinessException;

	void dropBlackListExpression(BlackListExpression expression) throws BusinessException;

}
