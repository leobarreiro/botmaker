package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javaleo.systems.botmaker.ejb.entities.BlackListExpression;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;

@Named
@ConversationScoped
public class BlackListExpressionAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PAGE_EDIT = "/pages/expressions/expression.jsf?faces-redirect=true";
	public static final String PAGE_DETAIL = "/pages/expressions/expression-detail.jsf?faces-redirect=true";
	public static final String PAGE_LIST = "/pages/expressions/expression-search.jsf?faces-redirect=true";

	@Inject
	private Conversation conversation;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private UserPreferenceAction preferenceAction;
	
	@Inject
	private MsgAction msgAction;

	private BlackListExpression expression;
	private List<BlackListExpression> expressionList;
	private ScriptType scriptType;
	private String exampleCode;
	private String debugContent;

	public String startNew() {
		loadOptions();
		if (conversation.isTransient()) {
			conversation.begin();
		}
		expression = new BlackListExpression();
		expression.setScriptType(scriptType);
		return PAGE_EDIT;
	}

	public String detail(BlackListExpression expr) {
		this.expression = expr;
		return PAGE_DETAIL;
	}

	public String edit(BlackListExpression expr) {
		this.expression = expr;
		return PAGE_EDIT;
	}

	public String save() {
		try {
			facade.saveBlackListExpression(expression);
			msgAction.addInfoMessage("Expression saved.");
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		return PAGE_DETAIL;
	}

	public String list() {
		if (!conversation.isTransient()) {
			conversation.end();
			conversation.begin();
		} else {
			conversation.begin();
		}
		loadOptions();
		expressionList = facade.listBlackListExpressionByScriptType(scriptType);
		return PAGE_LIST;
	}

	public void testExpression() {
		try {
			facade.testScriptAgainstBlackListExpression(exampleCode, scriptType);
			debugContent = "Ok! Approved.";
		} catch (BusinessException e) {
			debugContent = e.getMessage();
		}
	}
	
	public String dropExpression() {
		try {
			this.scriptType = expression.getScriptType();
			facade.dropBlackListExpression(expression);
			msgAction.addInfoMessage("Expression droped");
			return list();
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return PAGE_DETAIL;
		}
	}
	
	public void clear() {
		debugContent = "";
	}

	private void loadOptions() {
		preferenceAction.loadPreferences();
		if (scriptType == null) {
			scriptType = ScriptType.PYTHON;
		}
	}

	public BlackListExpression getExpression() {
		return expression;
	}

	public void setExpression(BlackListExpression expression) {
		this.expression = expression;
	}

	public List<BlackListExpression> getExpressionList() {
		return expressionList;
	}

	public void setExpressionList(List<BlackListExpression> expressionList) {
		this.expressionList = expressionList;
	}

	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	public String getExampleCode() {
		return exampleCode;
	}

	public void setExampleCode(String exampleCode) {
		this.exampleCode = exampleCode;
	}

	public String getDebugContent() {
		return debugContent;
	}

	public void setDebugContent(String debugContent) {
		this.debugContent = debugContent;
	}

}
