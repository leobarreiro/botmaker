package com.javaleo.systems.botmaker.ejb.security;

import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Policy;

public class BotMakerScriptPolicy extends Policy {

	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
		// TODO Auto-generated method stub
		return super.getPermissions(codesource);
	}

	
	
}
