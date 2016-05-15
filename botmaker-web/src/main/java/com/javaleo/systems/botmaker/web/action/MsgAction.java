package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

@Named
@ConversationScoped
public class MsgAction implements Serializable {

	private static final long serialVersionUID = -8826748452551615678L;

	public enum MessageType {
		INFO, WARN, ERROR;
	}

	private String msg;
	private MessageType type;
	private boolean mustShow;

	public void addMessage(MessageType type, String msg) {
		this.type = type;
		this.msg = msg;
		this.mustShow = true;
	}

	public void addInfoMessage(String msg) {
		addMessage(MessageType.INFO, msg);
	}

	public void addErrorMessage(String msg) {
		addMessage(MessageType.ERROR, msg);
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

	public String showMsg() {
		this.mustShow = false;
		return msg;
	}

	public String getMsg() {
		return msg;
	}

	public MessageType getType() {
		return type;
	}

	public boolean isMustShow() {
		return mustShow;
	}

	public void setMustShow(boolean mustShow) {
		this.mustShow = mustShow;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
