package com.javaleo.systems.botmaker.ejb.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.pojos.DialogContextVar;

@Stateless
public class DialogContextVarBusiness implements IDialogContextVarBusiness {

	private static final long serialVersionUID = 1L;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<DialogContextVar> getListDialogContextVars() {
		List<DialogContextVar> contextVars = new ArrayList<DialogContextVar>();
		contextVars.add(new DialogContextVar("idChat", "0", "Integer value from Telegram Chat who origins the Dialog."));
		contextVars.add(new DialogContextVar("dateInMilis", Long.toString(Calendar.getInstance().getTimeInMillis()),
				"Integer value who represents the datetime (in milliseconds) that the Dialog starts."));
		contextVars.add(new DialogContextVar("userId", "0", "Integer value from Telegram User. When the chat is a private chat, idChat and userId have the same value."));
		contextVars.add(new DialogContextVar("userAnswer", "", "String value with the user answer content."));
		return contextVars;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<DialogContextVar> getContextVarsFromDialog(Dialog dialog) {
		// TODO Auto-generated method stub
		return null;
	}

}
