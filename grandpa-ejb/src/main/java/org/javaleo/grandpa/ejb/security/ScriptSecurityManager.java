package org.javaleo.grandpa.ejb.security;


public class ScriptSecurityManager extends SecurityManager {

	public ScriptSecurityManager() {
		//super();
	}

	@Override
	public void checkExit(int status) {
		throw new SecurityException("Instruction not allowed by BotMaker.");
	}

	// @Override
	// public void checkExec(String cmd) {
	// super.checkExec(cmd);
	// }
	//
	// @Override
	// public void checkWrite(FileDescriptor fd) {
	// super.checkWrite(fd);
	// }
	//
	// @Override
	// public void checkWrite(String file) {
	// super.checkWrite(file);
	// }
	//
	// @Override
	// public void checkDelete(String file) {
	// super.checkDelete(file);
	// }
	//
	// @Override
	// public void checkPackageAccess(String pkg) {
	// super.checkPackageAccess(pkg);
	// }
	//
	// @Override
	// public void checkSecurityAccess(String target) {
	// super.checkSecurityAccess(target);
	// }

}
