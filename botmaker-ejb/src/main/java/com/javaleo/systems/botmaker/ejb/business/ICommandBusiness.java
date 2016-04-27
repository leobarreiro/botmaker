package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Local
public interface ICommandBusiness extends Serializable {

	List<Command> listCommandsByBot(Bot bot);

	void saveCommand(Command command) throws BusinessException;

	Command getCommandByBotAndKey(Bot bot, String text);

	List<List<String>> convertCommandsToOptions(Bot bot);

	void dropCommand(Command command);

}
