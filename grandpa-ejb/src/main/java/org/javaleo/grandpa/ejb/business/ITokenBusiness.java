package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Token;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface ITokenBusiness extends Serializable {

	Token generateTokenToUser(User user) throws BusinessException;

	Token getTokenByUUID(final String uuid) throws BusinessException;

}
