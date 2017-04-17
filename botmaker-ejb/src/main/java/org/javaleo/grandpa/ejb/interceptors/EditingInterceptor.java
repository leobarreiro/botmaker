package org.javaleo.grandpa.ejb.interceptors;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.javaleo.grandpa.ejb.annotations.EditingBinding;
import org.javaleo.grandpa.ejb.annotations.EditingNow;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;

@Interceptor
@EditingBinding
public class EditingInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BotMakerCredentials credentials;

	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {
		if (ctx.getMethod().getAnnotation(EditingNow.class) != null) {
			if (ctx.getMethod().getAnnotation(EditingNow.class).edit()) {
				credentials.startEditing();
			} else {
				credentials.stopEditing();
			}
		} else {
			credentials.stopEditing();
		}
		return ctx.proceed();
	}

}
