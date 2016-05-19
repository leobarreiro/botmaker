package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

@Local
public interface ICommandBusiness extends Serializable {

	List<Command> listCommandsByBot(Bot bot);

	void saveCommand(Command command) throws BusinessException;

	Command getCommandById(Long id);

	Command getCommandByBotAndKey(Bot bot, String text);

	List<List<String>> convertCommandsToOptions(Bot bot);

	void dropCommand(Command command);

	void postProcessCommand(Dialog dialog, Command command);

}
