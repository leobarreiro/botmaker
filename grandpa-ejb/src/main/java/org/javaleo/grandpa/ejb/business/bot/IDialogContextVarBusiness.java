package org.javaleo.grandpa.ejb.business.bot;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.pojos.DialogContextVar;

@Local
public interface IDialogContextVarBusiness extends Serializable {

	List<DialogContextVar> getListDialogContextVars();

	List<DialogContextVar> getContextVarsFromDialog(Dialog dialog);

}
