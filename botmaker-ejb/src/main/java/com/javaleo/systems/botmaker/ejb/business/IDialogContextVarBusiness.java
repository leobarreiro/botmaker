package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.pojos.DialogContextVar;

@Local
public interface IDialogContextVarBusiness extends Serializable {

	List<DialogContextVar> getListDialogContextVars();

	List<DialogContextVar> getContextVarsFromDialog(Dialog dialog);

}
