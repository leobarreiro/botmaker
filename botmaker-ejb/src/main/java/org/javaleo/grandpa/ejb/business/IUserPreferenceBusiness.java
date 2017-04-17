package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.entities.UserPreference;

@Local
public interface IUserPreferenceBusiness extends Serializable {

	List<UserPreference> listPreferencesByUser(User user);
	
	UserPreference getPreferenceByUserAndName(User user, String name);

	void savePreference(User user, UserPreference userPreference);

	void removePreference(UserPreference userPreference);

}
