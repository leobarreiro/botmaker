package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.entities.UserPreference;

@Local
public interface IUserPreferenceBusiness extends Serializable {

	List<UserPreference> listPreferencesByUser(User user);
	
	UserPreference getPreferenceByUserAndName(User user, String name);

	void savePreference(User user, UserPreference userPreference);

	void removePreference(UserPreference userPreference);

}
