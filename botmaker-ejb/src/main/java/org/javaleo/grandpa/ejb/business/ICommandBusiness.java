package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Bot;
import org.javaleo.grandpa.ejb.entities.Command;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.pojos.Dialog;

@Local
public interface ICommandBusiness extends Serializable {

	List<Command> listCommandsByBot(Bot bot);

	void saveCommand(Command command) throws BusinessException;

	Command getCommandById(Long id);

	Command getCommandByBotAndKey(Bot bot, String text);

	List<List<String>> convertCommandsToOptions(Bot bot);

	void dropCommand(Command command);

	void postProcessCommand(Dialog dialog, Command command) throws BusinessException;

}
