package org.javaleo.grandpa.ejb.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.schedules.ManagerUtils;
import org.slf4j.Logger;

@Named
@Stateless
public class DevUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;
	
	@Inject
	private ManagerUtils managerUtils;

	private Long botId;
	private Integer dialogId;
	private Map<String, String> vars;
	
	@PostConstruct
	public void initialize() {
		this.vars = new HashMap<String, String>();
	}

	public void configureBotAndDialog(Long botId, Integer dialogId) {
		this.botId = botId;
		this.dialogId = dialogId;
		LOG.info(this.vars.toString());
	}
	
	public boolean setVar(String name, String value) {
		vars.put(name, value);
		managerUtils.hashCode();
		return true;
	}

	public Long getBotId() {
		return botId;
	}

	public void setBotId(Long botId) {
		this.botId = botId;
	}

	public Integer getDialogId() {
		return dialogId;
	}

	public void setDialogId(Integer dialogId) {
		this.dialogId = dialogId;
	}

}
