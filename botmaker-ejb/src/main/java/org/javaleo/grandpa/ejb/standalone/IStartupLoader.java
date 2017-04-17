package org.javaleo.grandpa.ejb.standalone;

import java.io.Serializable;

import javax.ejb.Local;

@Local
public interface IStartupLoader extends Serializable {

	void startVerification();

}
