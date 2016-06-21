package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Token;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Local
public interface ITokenBusiness extends Serializable {

	Token generateTokenToUser(User user) throws BusinessException;

	Token validateTokenByUUID(final String uuid) throws BusinessException;

}
