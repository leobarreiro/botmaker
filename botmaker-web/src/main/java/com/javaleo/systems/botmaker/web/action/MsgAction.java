package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

@Named
@SessionScoped
public class MsgAction implements Serializable {

	private static final long serialVersionUID = -8826748452551615678L;

	public enum MessageType {
		INFO, WARN, ERROR;
	}

	private String msg;
	private MessageType type;
	private String titleModal;

	public void addMessage(MessageType type, String msg) {
		this.type = type;
		this.msg = msg;
		if (isError()) {
			titleModal = "Erro";
		} else if (isWarn()) {
			titleModal = "Atenção";
		} else {
			titleModal = "Informação";
		}
	}

	public void clear() {
		msg = null;
	}

	public boolean isError() {
		return (type != null && type.equals(MessageType.ERROR));
	}

	public boolean isWarn() {
		return (type != null && type.equals(MessageType.WARN));
	}

	public boolean isInfo() {
		return (type != null && type.equals(MessageType.INFO));
	}

	public boolean hasMsg() {
		return (StringUtils.isNotBlank(msg));
	}

	public void show() {
		if (hasMsg()) {
			RequestContext.getCurrentInstance().execute("PF('msgDialog').show();");
			clear();
		}
	}

	public void showInDialog() {
		if (hasMsg()) {
			FacesMessage.Severity severity;
			if (isError()) {
				severity = FacesMessage.SEVERITY_ERROR;
			} else if (isWarn()) {
				severity = FacesMessage.SEVERITY_WARN;
			} else {
				severity = FacesMessage.SEVERITY_INFO;
			}
			FacesMessage facesMessage = new FacesMessage(severity, titleModal, msg);
			RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
			clear();
		}
	}

	public String getMsg() {
		return msg;
	}

	public MessageType getType() {
		return type;
	}

	public String getTitleModal() {
		return titleModal;
	}

}
