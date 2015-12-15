package edu.sysu.netlab.livestreaming.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.log.Logger;

public class ExceptionInterceptor implements Interceptor {

	private static final Logger lOGGER = Logger.getLogger(LoginInterceptor.class);
	
	@Override
	public void intercept(Invocation inv) {
		try {
			inv.invoke();
		} catch (Exception e) {
			lOGGER.error(inv.getActionKey(), e);
		}
	}

}
